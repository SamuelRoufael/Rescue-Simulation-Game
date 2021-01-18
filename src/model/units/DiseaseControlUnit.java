package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Injury;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class DiseaseControlUnit extends MedicalUnit {

	public DiseaseControlUnit(String unitID, Address location,
			int stepsPerCycle, WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
				getTarget().getDisaster().setActive(false);
				Citizen target = (Citizen) getTarget();
				if (target.getHp() == 0) {
					jobsDone();
					return;
				} else if (target.getToxicity() > 0) {
					target.setToxicity(target.getToxicity() - getTreatmentAmount());
					if (target.getToxicity() == 0)
						target.setState(CitizenState.RESCUED);
				}

				else if (target.getToxicity() == 0)
					heal();
			}

	public void respond(Rescuable r) throws IncompatibleTargetException, CannotTreatException {
		if(r instanceof ResidentialBuilding) {
			throw new IncompatibleTargetException(this,this.getTarget(),"DiseaseControlUnit can not respond to a ResidentialBuilding");
		}
		else if (((Citizen)r).getDisaster() instanceof Injury && ((Citizen)r).getBloodLoss()!=0) {
			throw new CannotTreatException(this,this.getTarget(),"please choose correct unit");
		}
		else if(this.canNotTreat(this.getTarget())==true || ((Citizen)r).getState().equals(CitizenState.SAFE)) {
			throw new CannotTreatException(this,this.getTarget(),"Citizen is already safe");
		}
		else {
			if (getTarget() != null && ((Citizen) getTarget()).getToxicity() > 0
					&& getState() == UnitState.TREATING)
				reactivateDisaster();
			finishRespond(r);
		}
	}

}
