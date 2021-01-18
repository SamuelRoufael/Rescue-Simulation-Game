package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CannotTreatException;
import exceptions.CitizenAlreadyDeadException;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Disaster implements Simulatable{
	private int startCycle;
	private Rescuable target;
	private boolean active;
	public Disaster(int startCycle, Rescuable target) {
		this.startCycle = startCycle;
		this.target = target;
	}
	public String toString() {
		String s = "";
		if(this instanceof Fire && this.getTarget()!=null)
			s+= "Fire on Building at location : " +((ResidentialBuilding)this.getTarget()).getLocation();
		if(this instanceof Collapse && this.getTarget()!=null)
			s+= "Collapse on Building at location : " +((ResidentialBuilding)this.getTarget()).getLocation();
		if(this instanceof GasLeak && this.getTarget()!=null)
			s+= "GasLeak on Building at location : " +((ResidentialBuilding)this.getTarget()).getLocation();
		if(this instanceof Injury && this.getTarget()!=null)
			s+= "Citizen has Injury at location : " +((Citizen)this.getTarget()).getLocation();
		if(this instanceof Infection && this.getTarget()!=null)
			s+= "Citizen has Infection at location : " +((Citizen)this.getTarget()).getLocation();
		return s;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public int getStartCycle() {
		return startCycle;
	}
	public Rescuable getTarget() {
		return target;
	}
	public void strike() throws CitizenAlreadyDeadException, BuildingAlreadyCollapsedException, CannotTreatException 
	{
		
		target.struckBy(this);
		active=true;
	}
}
