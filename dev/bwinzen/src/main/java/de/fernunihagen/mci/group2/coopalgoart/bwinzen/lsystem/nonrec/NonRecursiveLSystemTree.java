package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.nonrec;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.AlphabetLetter;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.LSystem;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.LSystemTree;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.PrecalculationableLSystemTree;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.nonrec.calculation.CalculationRule;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.nonrec.calculation.CalculationState;
import processing.core.PGraphics;
import processing.core.PVector;

public class NonRecursiveLSystemTree extends LSystemTree implements PrecalculationableLSystemTree {
	private NavigableMap<Integer, Future<LSystemTree>> cacheFuture = new TreeMap<>();
	private final Executor executorTree = Executors.newSingleThreadExecutor(new ThreadFactory() {
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setName("NonRecursiveLSystemTree-Calculation"+UUID.randomUUID());
			t.setDaemon(true);
			return t;
		}
	});
	private List<CalculationState> states = new LinkedList<>();
	private Map<AlphabetLetter, CalculationRule> calcRules = new HashMap<>();
	private float size = 1;

	public NonRecursiveLSystemTree(LSystem lSystem, List<CalculationRule> calcRules, float size) {
		super(lSystem);
		HashSet<AlphabetLetter> set = new HashSet<>(lSystem.getAlphabet());
		calcRules.stream() //
				.map(CalculationRule::getInputs)// convert rule to the input it will be used for
				.flatMap(Arrays::stream) //
				.forEach(set::remove); // removes all values which are covered by rules
		if (!set.isEmpty()) { // should be empty otherwise the alphabet has signs which have no rules
			throw new IllegalArgumentException("Visualization Rules does not fit to alphabet");
		}
		this.lSystem = lSystem;
		this.size = size;
		calcRules.forEach(r -> {
			AlphabetLetter[] inputs = r.getInputs();
			CalculationRule clone = r.cloneIfNeccessary();
			for (int i = 0; i<inputs.length;i++) {
				this.calcRules.put(inputs[i], clone);
			}
		});
		FutureTask<LSystemTree> future = new FutureTask<>(()->this);
		future.run();
		cacheFuture.put(lSystem.getIterationCounter(), future);
	}

	private NonRecursiveLSystemTree(LSystem nextIteration, NonRecursiveLSystemTree parent, Collection<CalculationRule> calcRules) {
		super(nextIteration);
		calcRules.forEach(r -> {
			AlphabetLetter[] inputs = r.getInputs();
			CalculationRule clone = r.cloneIfNeccessary();
			for (int i = 0; i<inputs.length;i++) {
				this.calcRules.put(inputs[i], clone);
			}
		});
		this.size = parent.size;
		this.cacheFuture = parent.cacheFuture;
		calculateStates();
		this.colorFactory = parent.colorFactory;
		this.offsetX = parent.offsetX;
		this.offsetY = parent.offsetY;
	}

	@Override
	public void draw(CooperationContext context, PGraphics pg) {
		if (states.isEmpty()) {
			calculateStates();
		}
//			shape = pg.createShape();
//			shape.beginShape();
//			CalculationState previousState = null;
//			for (CalculationState state : states) {
//				if (!state.isWentBack()) {
//					PVector pos = previousState.getPosition();
//					shape.vertex(pos.x, pos.y);
//				}
//				previousState = state;
//			}
//			if (!previousState.isWentBack()) {
//				PVector pos = previousState.getPosition();
//				shape.vertex(pos.x, pos.y);
//			}
//			shape.endShape();
		CalculationState previousState = null;
		for (CalculationState state : states) {
			if (!state.isWentBack()) {
				drawLine(pg, previousState, state);
			}
			previousState = state;
		}
	}

	private void drawLine(PGraphics pg, CalculationState previousState, CalculationState state) {
		PVector position1 = previousState.getPosition();
		PVector position2 = state.getPosition();
		if(colorFactory != null) {
			pg.stroke(colorFactory.color(position1, position2));
		}
		if (position1 != position2) { // in case of rotation no line is needed
			pg.line(position1.x, position1.y, position2.x, position2.y);
		}
	}

	private void calculateStates() {
		List<AlphabetLetter> currentSequence = lSystem.getCurrentSequence();
		CalculationState state = new CalculationState(new PVector(), 0, true);

		for (AlphabetLetter l : currentSequence) {
			states.add(state);
			state = this.calcRules.get(l).calculate(l, lSystem, state, size);
		}
		states.add(state);
//		System.out.println("Before:"+states.size());
//		optimize();
//		System.out.println("After:"+states.size());
	}

	private void optimize() {
		List<CalculationState> optimized = new LinkedList<>();
		CalculationState lastState = null;
		for(CalculationState state:states) {
			if(lastState != null) {
				if(state.isWentBack() && !lastState.getPosition().equals(state.getPosition()) && lastState.getAngle()!=state.getAngle()) {
					optimized.add(lastState);
					//Position is equal => we can merge =>skip last 
				}else if(!lastState.getPosition().equals(state.getPosition()) || lastState.getAngle() != state.getAngle()) { 
					optimized.add(lastState);
				}
			}
			lastState = state;
			continue;
		}
		optimized.add(lastState);
		states = optimized;
	}

	@Override
	public LSystemTree next() {
		synchronized (cacheFuture) {
			try {
				int it = lSystem.getIterationCounter() + 1;
				Future<LSystemTree> future = cacheFuture.get(it);
				if(future == null) {
					future = calculateUpToIteration(it);
				}
				return future.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	@Override
	public LSystemTree previous() {
		synchronized (cacheFuture) {
			Future<LSystemTree> future = cacheFuture.get(lSystem.getIterationCounter() - 1);
			try {
				return future.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	@Override
	public synchronized Future<LSystemTree> calculateUpToIteration(int iterationCount)
			throws InterruptedException, ExecutionException {
		int i = lSystem.getIterationCounter();
		LSystem lSystemToStartWith = lSystem;
		if (!cacheFuture.isEmpty()) {
			Entry<Integer, Future<LSystemTree>> lastEntry = cacheFuture.lastEntry();
			if (lastEntry.getKey() > lSystem.getIterationCounter()) {
				i = lSystem.getIterationCounter();
				lSystemToStartWith = lastEntry.getValue().get().getLSystem();
			}
		}
		LSystem finalLSystemToStartWith = lSystemToStartWith;
		FutureTask<LSystemTree> futureTask = null;
		for (int j = i+1 ; j <= iterationCount; j++) {
			FutureTask<LSystemTree> finalFutureTask = futureTask;
			futureTask = new FutureTask<>(new Callable<LSystemTree>() {
				@Override
				public LSystemTree call() throws Exception {
					LSystem nextIteration = finalLSystemToStartWith.nextIteration();
					if(finalFutureTask != null) {
						nextIteration = finalFutureTask.get().getLSystem().nextIteration();
					}
					NonRecursiveLSystemTree nextTree =  new NonRecursiveLSystemTree(nextIteration, NonRecursiveLSystemTree.this, calcRules.values());
//					System.out.println("Iteration: "+ nextTree.getLSystem().getIterationCounter());
					return nextTree;
				}
			});
			cacheFuture.put(j, futureTask);
			executorTree.execute(futureTask);
		}
		return futureTask;
	}
	
	@Override
	public boolean isMultiColorAble() {
		return true;
	}

}
