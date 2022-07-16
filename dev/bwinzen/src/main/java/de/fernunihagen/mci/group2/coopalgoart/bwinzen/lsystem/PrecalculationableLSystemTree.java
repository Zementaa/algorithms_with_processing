package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface PrecalculationableLSystemTree {
	Future<LSystemTree> calculateUpToIteration(int i) throws InterruptedException, ExecutionException;
}
