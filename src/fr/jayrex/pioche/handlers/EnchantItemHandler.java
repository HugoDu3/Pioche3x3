package fr.jayrex.pioche.handlers;

import fr.jayrex.pioche.Main;
import fr.jayrex.pioche.listeners.EnchantItemListener;

public class EnchantItemHandler {
	public EnchantItemHandler() {};
	public EnchantItemListener listener;

	public void Init(Main plugin) {
		listener = new EnchantItemListener(plugin);
	}

	public EnchantItemListener getListener() {
		return listener;
	}
}
