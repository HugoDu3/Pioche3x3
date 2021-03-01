package fr.jayrex.pioche.handlers;

import fr.jayrex.pioche.Main;
import fr.jayrex.pioche.listeners.BlockBreakListener;

public class BlockBreakHandler {
	public BlockBreakHandler() {};
	public BlockBreakListener listener;

	public void Init(Main plugin) {
		listener = new BlockBreakListener(plugin);
	}

	public BlockBreakListener getListener() {
		return listener;
	}
}
