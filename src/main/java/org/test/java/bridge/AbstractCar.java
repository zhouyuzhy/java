package org.test.java.bridge;

public abstract class AbstractCar {

	public AbstractCar(AbstractEngine engine) {
		this.engine = engine;
	}
	
	private AbstractEngine engine;
	private String name;
	
	public void setEngine(AbstractEngine engine) {
		this.engine = engine;
	}
	
	public AbstractEngine getEngine() {
		return engine;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return this.name + " with engine " + this.engine;
	}
}
