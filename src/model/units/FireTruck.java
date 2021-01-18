package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.Fire;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;

public class FireTruck extends FireUnit {

	public FireTruck(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
				getTarget().getDisaster().setActive(false);

				ResidentialBuilding target = (ResidentialBuilding) getTarget();
				if (target.getStructuralIntegrity() == 0) {
					jobsDone();
					return;
				} else if (target.getFireDamage() > 0)

					target.setFireDamage(target.getFireDamage() - 10);

				if (target.getFireDamage() == 0)

					jobsDone();
			}
	public void respond(Rescuable r) throws IncompatibleTargetException, CannotTreatException {
		if(r instanceof Citizen) {
			throw new IncompatibleTargetException(this,this.getTarget(),"FireTruck can not respond to a Citizen");
		}
		else if (!(((ResidentialBuilding)r).getDisaster() instanceof Fire)) {
			throw new CannotTreatException(this,this.getTarget(),"please choose correct unit");
		}
		else if(r instanceof ResidentialBuilding && this.canNotTreat(this.getTarget())==true) {
			throw new CannotTreatException(this,this.getTarget(),"Building is not in danger");
		}
		else
			super.respond(r);
	}

}
