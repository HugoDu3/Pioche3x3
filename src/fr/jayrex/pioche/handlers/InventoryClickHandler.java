package fr.jayrex.pioche.handlers;

import fr.jayrex.pioche.Main;
import fr.jayrex.pioche.listeners.InventoryClickListener;

public class InventoryClickHandler {
	public InventoryClickHandler() {};
	public InventoryClickListener listener;

	public void Init(Main plugin) {
		listener = new InventoryClickListener(plugin);
	}

	public InventoryClickListener getListener() {
		return listener;
	}
}
