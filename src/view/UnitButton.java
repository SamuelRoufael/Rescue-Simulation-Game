package view;

import javax.swing.JButton;

import model.units.Unit;

public class UnitButton extends JButton {
	Unit unit;
	
	public UnitButton (Unit unit) {
		this.unit = unit;
	}

	public Unit getUnit() {
		return unit;
	}
	
	
}
