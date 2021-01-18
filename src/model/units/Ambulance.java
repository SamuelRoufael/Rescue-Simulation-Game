package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Infection;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class Ambulance extends MedicalUnit {

	public Ambulance(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat()  {
				getTarget().getDisaster().setActive(false);

				Citizen target = (Citizen) getTarget();
				if (target.getHp() == 0) {
					jobsDone();
					return;
				} else if (target.getBloodLoss() > 0) {
					target.setBloodLoss(target.getBloodLoss() - getTreatmentAmount());
					if (target.getBloodLoss() == 0)
						target.setState(CitizenState.RESCUED);
				}

				else if (target.getBloodLoss() == 0)
					heal();
	}

	public void respond(Rescuable r) throws IncompatibleTargetException, CannotTreatException {
		if(r instanceof ResidentialBuilding) {
			throw new IncompatibleTargetException(this,this.getTarget(),"Ambulance can not respond to a ResidentialBuilding");
		}
		else if (((Citizen)r).getDisaster() instanceof Infection )
			throw new CannotTreatException(this,this.getTarget(),"please choose correct unit");
		else if(this.canNotTreat(this.getTarget())==true || ((Citizen)r).getState().equals(CitizenState.SAFE)) {
			throw new CannotTreatException(this,this.getTarget(),"Citizen is already safe");
		}  
		else {
			if (getTarget() != null && ((Citizen) getTarget()).getBloodLoss() > 0
					&& getState() == UnitState.TREATING)
				reactivateDisaster();
			finishRespond(r);
		}
	}

}
