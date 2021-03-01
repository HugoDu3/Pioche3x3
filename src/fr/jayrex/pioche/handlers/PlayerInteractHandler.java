package fr.jayrex.pioche.handlers;

import fr.jayrex.pioche.Main;
import fr.jayrex.pioche.listeners.PlayerInteractListener;

public class PlayerInteractHandler {
	public PlayerInteractHandler() {};
	public PlayerInteractListener listener;

	public void Init(Main plugin) {
		listener = new PlayerInteractListener(plugin);
	}

	public PlayerInteractListener getListener() {
		return listener;
	}
}
