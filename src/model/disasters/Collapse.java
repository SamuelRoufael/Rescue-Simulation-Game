package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CannotTreatException;
import exceptions.CitizenAlreadyDeadException;
import model.infrastructure.ResidentialBuilding;


public class Collapse extends Disaster {

	public Collapse(int startCycle, ResidentialBuilding target) {
		super(startCycle, target);
		
	}
	public void strike() throws CannotTreatException, BuildingAlreadyCollapsedException, CitizenAlreadyDeadException 
	{
		ResidentialBuilding target= (ResidentialBuilding)getTarget();
		if(target.getStructuralIntegrity()<=0) {
			throw new BuildingAlreadyCollapsedException(this,"Building is already destroyed");
		}
		else {
			target.setFoundationDamage(target.getFoundationDamage()+10);
			super.strike();
		}
	}
	public void cycleStep()
	{
		ResidentialBuilding target= (ResidentialBuilding)getTarget();
		target.setFoundationDamage(target.getFoundationDamage()+10);
	}

}
