package simulation;

import exceptions.CannotTreatException;

public interface Simulatable {
public void cycleStep() throws CannotTreatException;
}
