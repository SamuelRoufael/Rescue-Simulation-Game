package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Disaster;
import model.events.SOSResponder;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Unit implements Simulatable, SOSResponder {
	private String unitID;
	private UnitState state;
	private Address location;
	private Rescuable target;
	private int distanceToTarget;
	private int stepsPerCycle;
	private WorldListener worldListener;
	
	public String toString() {
		String s = "";
		s+= "UnitID : "+ unitID + "\n";
		s+= "Unit state : "+ state + "\n";
		s+= "Unit location : "+ location + "\n";
		s+= "Unit distanceToTarget : "+ distanceToTarget + "\n";
		s+= "Unit stepsPerCycle : "+ stepsPerCycle + "\n";
		if(this instanceof Evacuator) {
			Evacuator e = (Evacuator) this;
			s+= "DistanceToBase : "+ e.getDistanceToBase()+"\n";
			s+= "MaxCapacity : "+ e.getMaxCapacity()+"\n";
			s+= "Number of Passengers : "+ e.getPassengers().size()+"\n";
			s+= "---Citizens IN Evacuator--- \n";
			if(e.getPassengers().size()==0)
				s+= "Empty \n";
			else {
				for(int i=0;i<e.getPassengers().size();i++)
					s+= e.getPassengers().get(i).toString() + "\n";
			}
		}
		if(target!=null) {
		s+= "----Target-------- \n";
		s+= target.toString();
		}
		return s;
		
	}
	
	public Unit(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		this.unitID = unitID;
		this.location = location;
		this.stepsPerCycle = stepsPerCycle;
		this.state = UnitState.IDLE;
		this.worldListener = worldListener;
	}

	public void setWorldListener(WorldListener listener) {
		this.worldListener = listener;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public UnitState getState() {
		return state;
	}

	public void setState(UnitState state) {
		this.state = state;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public String getUnitID() {
		return unitID;
	}

	public Rescuable getTarget() {
		return target;
	}

	public int getStepsPerCycle() {
		return stepsPerCycle;
	}

	public void setDistanceToTarget(int distanceToTarget) {
		this.distanceToTarget = distanceToTarget;
	}

	@Override
	public void respond(Rescuable r) throws IncompatibleTargetException, CannotTreatException {
		if (target != null && state == UnitState.TREATING)
			reactivateDisaster();
		finishRespond(r);

	}

	public void reactivateDisaster() {
		Disaster curr = target.getDisaster();
		curr.setActive(true);
	}

	public void finishRespond(Rescuable r) {
		target = r;
		state = UnitState.RESPONDING;
		Address t = r.getLocation();
		distanceToTarget = Math.abs(t.getX() - location.getX())
				+ Math.abs(t.getY() - location.getY());

	}

	public abstract void treat();

	public void cycleStep() throws CannotTreatException {
		if (state == UnitState.IDLE)
			return;
		if (distanceToTarget > 0) {
			distanceToTarget = distanceToTarget - stepsPerCycle;
			if (distanceToTarget <= 0) {
				distanceToTarget = 0;
				Address t = target.getLocation();
				worldListener.assignAddress(this, t.getX(), t.getY());
			}
		} else {
			state = UnitState.TREATING;
			treat();
		}
	}

	public void jobsDone() {
		target = null;
		state = UnitState.IDLE;

	}
	
	public boolean canNotTreat(Rescuable r) {
		if(r instanceof Citizen) {
			Citizen c = (Citizen) r;
			if(c.getState().equals(CitizenState.SAFE))
				return true;
		}
		if(r instanceof ResidentialBuilding) {
			ResidentialBuilding b = (ResidentialBuilding) r;
			if((b.getFoundationDamage()==0 && b.getFireDamage()==0 && b.getGasLevel()>0))
				return true;
		}
		return false;
	}
}
