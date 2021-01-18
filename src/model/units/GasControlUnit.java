package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;

public class GasControlUnit extends FireUnit {

	public GasControlUnit(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	public void treat() {
				getTarget().getDisaster().setActive(false);

				ResidentialBuilding target = (ResidentialBuilding) getTarget();
				if (target.getStructuralIntegrity() == 0) {
					jobsDone();
					return;
				} else if (target.getGasLevel() > 0) 
					target.setGasLevel(target.getGasLevel() - 10);

				if (target.getGasLevel() == 0)
					jobsDone();
			}
	public void respond(Rescuable r) throws IncompatibleTargetException, CannotTreatException {
		if(r instanceof Citizen) {
			throw new IncompatibleTargetException(this,this.getTarget(),"GasControlUnit can not respond to a Citizen");
		}
		else if (!(((ResidentialBuilding)r).getDisaster() instanceof GasLeak)) {
			throw new CannotTreatException(this,this.getTarget(),"please choose correct unit");
		}
		else if(this.canNotTreat(this.getTarget())==true) {
			throw new CannotTreatException(this,this.getTarget(),"Building is not in danger");
		}
		else
			super.respond(r);
	}

}
