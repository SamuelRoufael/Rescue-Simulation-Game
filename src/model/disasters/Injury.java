package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CannotTreatException;
import exceptions.CitizenAlreadyDeadException;
import model.people.Citizen;
import model.people.CitizenState;


public class Injury extends Disaster {

	public Injury(int startCycle, Citizen target) {
		super(startCycle, target);
	}
	@Override
	public void strike() throws CitizenAlreadyDeadException, BuildingAlreadyCollapsedException, CannotTreatException {
		Citizen target = (Citizen)getTarget();
		if(target.getState()==CitizenState.DECEASED) {
			throw new CitizenAlreadyDeadException(this,"Citizen is Already Dead");
		}
		else {
			target.setBloodLoss(target.getBloodLoss()+30);
			super.strike();
		}
	}
	@Override
	public void cycleStep() {
		Citizen target = (Citizen)getTarget();
		target.setBloodLoss(target.getBloodLoss()+10);
		
	}

}
