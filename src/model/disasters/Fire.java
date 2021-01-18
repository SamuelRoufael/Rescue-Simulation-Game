package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CannotTreatException;
import exceptions.CitizenAlreadyDeadException;
import model.infrastructure.ResidentialBuilding;


public class Fire extends Disaster {

	public Fire(int startCycle, ResidentialBuilding target) {
		super(startCycle, target);
		
	}
	@Override
	public void strike() throws BuildingAlreadyCollapsedException, CitizenAlreadyDeadException, CannotTreatException
	{
		ResidentialBuilding target= (ResidentialBuilding)getTarget();
		if(target.getStructuralIntegrity()<=0) {
			throw new BuildingAlreadyCollapsedException(this,"Building is already destroyed");
		}
		else {
			target.setFireDamage(target.getFireDamage()+10);
			super.strike();
		}
	}

	@Override
	public void cycleStep() {
		ResidentialBuilding target= (ResidentialBuilding)getTarget();
		target.setFireDamage(target.getFireDamage()+10);
		
	}

}
