package com.gmail.nuclearcat1337.base;

public class KitRunnable implements Runnable {
	
	private AnniKit kit;
	
	public KitRunnable(AnniKit kit)
	{
		this.kit = kit;
	}

	@Override
	public void run() {
		kit.passive();
	}
	
	public void stop()
	{
		this.stop();
	}

}
