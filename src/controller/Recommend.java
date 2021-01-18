package controller;

import java.util.ArrayList;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.Unit;
import model.units.UnitState;
import simulation.Rescuable;


public class Recommend {
	private CommandCenter commandCenter;
	private ArrayList<Unit> IDLEUnits = new ArrayList<>();
	private ArrayList<Unit> busyUnits= new ArrayList<>();
	private ArrayList<Citizen> citizens = new ArrayList<>();
	private ArrayList<ResidentialBuilding> buildings = new ArrayList<>();
	
	public Recommend(CommandCenter commandCenter) throws IncompatibleTargetException, CannotTreatException {
		this.commandCenter = commandCenter;
		splitUnits();
		updateBuildings();
		updateCitizens();
		respondUnits();
	}
	
	private  void splitUnits() {
		ArrayList<Unit> Units = commandCenter.getEmergencyUnits();
		for(int i=0;i<Units.size();i++) {
			if(Units.get(i).getState()==UnitState.IDLE) {
				if(IDLEUnits.isEmpty())
					IDLEUnits.add(Units.get(i));
				else {
					for(int j=0;j<IDLEUnits.size();j++) {
						if(Units.get(i).getStepsPerCycle()<=IDLEUnits.get(j).getStepsPerCycle()) {
							IDLEUnits.add(j,Units.get(i));
							break;
						}
					}
					if(!IDLEUnits.contains(Units.get(i)))
						IDLEUnits.add(Units.get(i));
				}
			}
			else
				busyUnits.add(Units.get(i));
		}
	}
	
	private void remainingCycles(Rescuable target) {
		int Cycles = 0;
		if(target instanceof Citizen) {
			Citizen citizen = (Citizen) target;
			int hp = citizen.getHp();
			int bloodLoss = citizen.getBloodLoss();
			int toxicity = citizen.getToxicity();
			while(hp>0) {
				Cycles++;
				if(citizen.getDisaster() instanceof Injury)
					bloodLoss+=10;
				if(citizen.getDisaster() instanceof Infection)
					toxicity+=15;
				if(bloodLoss==100 || toxicity==100) {
					hp = 0 ;
					break;
				}
				if(bloodLoss>0 && bloodLoss<30)
					hp-=5;
				else if(bloodLoss>=30 && bloodLoss<70)
					hp-=10;
				else if(bloodLoss >=70)
					hp-=15;
				if (toxicity >0 && toxicity < 30)
					hp-=5;
				else if(toxicity>=30 &&toxicity<70)
					hp-=10;
				else if(toxicity>=70)
					hp-=15;
			}
			citizen.setRemainingCycles(Cycles);
		}
		if(target instanceof ResidentialBuilding) {
			ResidentialBuilding building = (ResidentialBuilding) target;
			int structuralIntegrity = building.getStructuralIntegrity();
			int fireDamage = building.getFireDamage();
			int foundationDamage = building.getFoundationDamage();
			int gasLevel = building.getGasLevel();
			
			while(structuralIntegrity>0) {
				Cycles++;
				if(building.getDisaster() instanceof Fire) {
					fireDamage+=10;
				}
				if(building.getDisaster() instanceof GasLeak) {
					gasLevel+=15;
				}
				if(building.getDisaster() instanceof Collapse) {
					foundationDamage+=10;
				}
				if(foundationDamage==100 || gasLevel==85) {
					structuralIntegrity = 0;
					break;
				}
				if(foundationDamage>0){
					int damage= (int)((Math.random()*6)+5);
					structuralIntegrity-=damage;
					}
				if(fireDamage>0 &&fireDamage<30)
					structuralIntegrity-=3;
				else if(fireDamage>=30 &&fireDamage<70)
					structuralIntegrity-=5;
				else if(fireDamage>=70)
					structuralIntegrity-=7;
				}
			building.setRemainingCycles(Cycles);
		}
	}
	
	private int distanceToTarget(Unit unit,Rescuable target) {
		int X_Unit = unit.getLocation().getX();
		int Y_Unit = unit.getLocation().getY();
		int X_target = target.getLocation().getX();
		int Y_target = target.getLocation().getY();
		int distance = Math.abs(X_Unit-X_target)+Math.abs(Y_target-Y_Unit);
		return distance;
	}
	
	private void updateCitizens() {
		for(int i=0;i<commandCenter.getVisibleCitizens().size();i++) {
			Citizen citizen = commandCenter.getVisibleCitizens().get(i);
			remainingCycles(citizen);
			if(citizens.isEmpty())
				citizens.add(citizen);
			else {
				for(int j=0;j<citizens.size();j++) {
					if(citizen.getRemainingCycles()<=citizens.get(j).getRemainingCycles()) {
						citizens.add(j,citizen);
						break;
					}
				}
				if(!citizens.contains(citizen))
					citizens.add(citizen);
			}
		}
	}
	
	private void updateBuildings() {
		for(int i=0;i<commandCenter.getVisibleBuildings().size();i++) {
			ResidentialBuilding building = commandCenter.getVisibleBuildings().get(i);
			remainingCycles(building);
			if(buildings.isEmpty())
				buildings.add(building);
			else {
				for(int j=0;j<buildings.size();j++) {
					if(building.getRemainingCycles()<=buildings.get(j).getRemainingCycles()) {
						buildings.add(j,building);
						break;
					}
				}
				if(!buildings.contains(building))
					buildings.add(building);
			}
		}
	}

	private boolean canRescue(Unit unit,Rescuable target) {
		int distance = distanceToTarget(unit, target);
		int cycles = 0;
		while(distance>0) {
			distance -= unit.getStepsPerCycle();
			cycles++;
		}
		if(cycles >=target.getRemainingCycles())
			return false;
		return true;
	}

	private void respondUnits() throws IncompatibleTargetException, CannotTreatException {
		for(int i=0;i<IDLEUnits.size();i++) {
			if(IDLEUnits.get(i) instanceof Ambulance) {
				for(int j=0;j<citizens.size();j++) {
					if(citizens.get(j).getDisaster() instanceof Injury && canRescue(IDLEUnits.get(i),citizens.get(j))) {
						IDLEUnits.get(i).respond(citizens.get(j));
						citizens.remove(citizens.get(j));
						break;
					}
				}
			}
			if(IDLEUnits.get(i) instanceof DiseaseControlUnit) {
				for(int j=0;j<citizens.size();j++) {
					if(citizens.get(j).getDisaster() instanceof Infection && canRescue(IDLEUnits.get(i),citizens.get(j))) {
						IDLEUnits.get(i).respond(citizens.get(j));
						citizens.remove(citizens.get(j));
						break;
					}
				}
			}
			if(IDLEUnits.get(i) instanceof FireTruck) {
				for(int j=0;j<buildings.size();j++) {
					if(buildings.get(j).getDisaster() instanceof Fire && canRescue(IDLEUnits.get(i),buildings.get(j))) {
						IDLEUnits.get(i).respond(buildings.get(j));
						break;
					}
				}
			}
			if(IDLEUnits.get(i) instanceof GasControlUnit) {
				for(int j=0;j<buildings.size();j++) {
					if(buildings.get(j).getDisaster() instanceof GasLeak && canRescue(IDLEUnits.get(i),buildings.get(j))) {
						IDLEUnits.get(i).respond(buildings.get(j));
						buildings.remove(buildings.get(j));
						break;
					}
				}
			}
			if(IDLEUnits.get(i) instanceof Evacuator) {
				for(int j=0;j<buildings.size();j++) {
					if(buildings.get(j).getDisaster() instanceof Collapse && canRescue(IDLEUnits.get(i),buildings.get(j))) {
						IDLEUnits.get(i).respond(buildings.get(j));
						buildings.remove(buildings.get(j));
						break;
					}
				}
			}
		}

	}
}
