package view;

import model.disasters.Disaster;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import controller.CommandCenter;
import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CannotTreatException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.IncompatibleTargetException;
import model.units.Unit;
import simulation.Rescuable;

public class View extends JFrame implements ActionListener{
	private JPanel mainPanel;
	private JPanel WorldPanel;
	private JPanel AvailableUnitsPanel = new JPanel();
	private JPanel RespondingUnitsPanel = new JPanel();
	private JPanel TreatingUnitsPanel = new JPanel();
	private JPanel FunctionPanel;
	private JPanel GameData;
	private JPanel Dead;
	private JPanel CitzensPanel;
	private TextArea Info;
	private TextArea TActiveDisaster;
	private TextArea TExecutedDisaster;
	private JButton ButtonNextCycle;
	private JButton ButtonRespond;
	private JButton World [][];
	private JButton Help;
	private UnitButton UnitBT;
	private ArrayList<UnitButton> AvailableUnits = new ArrayList<>();
	private JLabel labelCurrentCycle;
	private JLabel labelcasualties;
	private CommandCenter commandCenter;
	private int currentCycle=0;
	private Unit selectedUnit;
	private Rescuable rescuable;
	
	public View() throws Exception {
		commandCenter= new CommandCenter();
		mainPanel = new JPanel(new BorderLayout(8,8));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,8,8));
		World = new JButton [10][10]; 
		//BorderFactory.createEmptyBorder(top, left, bottom, right);
		getContentPane().add(mainPanel,BorderLayout.CENTER);
		setTitle("RescueSimulation");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 0, 1800, 1000);
		//pack(); //sets JFrame to minimum size to display all elements inside it.
		JPanel UnitsPanel = new JPanel();
		createUnitsPanel(UnitsPanel);
		createInfo();
		createWorldPanel();
		NextCycleUpdate();
		createCitizenPanel();
		createHelp();
		setVisible(true);
		validate();
	}
	
	public void updateWorld() {
		ArrayList<ResidentialBuilding> buildings = commandCenter.getVisibleBuildings();
		ArrayList<Citizen> citizens = commandCenter.getVisibleCitizens();
		JButton button;
		ImageIcon image;
		Image img;
		Image dimg;
		int x;
		int y;
		for(int i=0;i<buildings.size();i++) {
			ResidentialBuilding building = buildings.get(i);
			x = buildings.get(i).getLocation().getX();
			y = buildings.get(i).getLocation().getY();
			button = World[x][y];
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Info.setText(building.toString());
					rescuable = building;
					CitzensPanel.removeAll();
					CitzensPanel.updateUI();
					for(int j=0;j<building.getOccupants().size();j++) {
						JButton citizenBT = new JButton(building.getOccupants().get(j).getName());
						citizenBT.setToolTipText("Citizen");
						CitzensPanel.add(citizenBT);
						CitzensPanel.validate();
						Citizen citizen = building.getOccupants().get(j);
						citizenBT.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								Info.setText(citizen.toString());
								rescuable = citizen;			
							}
						});
					}
				}
			});
			if(building.getStructuralIntegrity()==0) {
				image = new ImageIcon("buildingRIPjpg.jpg");
				img = image.getImage();
				dimg = img.getScaledInstance(button.getWidth(),button.getHeight(),Image.SCALE_SMOOTH);
				image.setImage(dimg);
				button.setIcon(image);
				button.setToolTipText("ResidentialBuilding");
			}
			else if(building.getStructuralIntegrity()>0 && (building.getFireDamage()>0 || building.getFoundationDamage()>0 || building.getGasLevel()>0)) {
				image = new ImageIcon("buildingd.jpg");
				img = image.getImage();
				dimg = img.getScaledInstance(button.getWidth(),button.getHeight(),Image.SCALE_SMOOTH);
				image.setImage(dimg);
				button.setIcon(image);
				button.setToolTipText("ResidentialBuilding");
			}
			else {
				image = new ImageIcon("pbuilding.jpg");
				img = image.getImage();
				dimg = img.getScaledInstance(button.getWidth(),button.getHeight(),Image.SCALE_SMOOTH);
				image.setImage(dimg);
				button.setIcon(image);
				button.setToolTipText("ResidentialBuilding");
			}
		}
		for(int i=0;i<citizens.size();i++) {
			boolean flag = false;
			Citizen citizen = citizens.get(i);
			x = citizen.getLocation().getX();
			y = citizen.getLocation().getY();
			for(int j=0;j<buildings.size();j++) {
				ResidentialBuilding building = buildings.get(j);
				int xb = building.getLocation().getX();
				int yb = building.getLocation().getY();
				if(xb==x && yb==y)
					flag = true;
			}
			if(flag==false) {
			button = World[x][y];
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Info.setText(citizen.toString());
					rescuable = citizen;
				}
			});
			if(citizen.getState()==CitizenState.IN_TROUBLE) {
				image = new ImageIcon("citzenD.jpg");
				img = image.getImage();
				dimg = img.getScaledInstance(button.getWidth(),button.getHeight(),Image.SCALE_SMOOTH);
				image.setImage(dimg);
				button.setIcon(image);
				button.setToolTipText("Citizen");
			}
			if(citizen.getState()==CitizenState.RESCUED) {
				image = new ImageIcon("citzenH.jpg");
				img = image.getImage();
				dimg = img.getScaledInstance(button.getWidth(),button.getHeight(),Image.SCALE_SMOOTH);
				image.setImage(dimg);
				button.setIcon(image);
				button.setToolTipText("Citizen");
			}
			if(citizen.getState()==CitizenState.DECEASED) {
				image = new ImageIcon("RIP.png");
				img = image.getImage();
				dimg = img.getScaledInstance(button.getWidth(),button.getHeight(),Image.SCALE_SMOOTH);
				image.setImage(dimg);
				button.setIcon(image);
				button.setToolTipText("Citizen");
			}
			}
			}
		
	}
	
	public void createCitizenPanel() {
		CitzensPanel =new JPanel();
		CitzensPanel.setLayout(new GridLayout(1,0));
		CitzensPanel.setBorder(BorderFactory.createEmptyBorder(10,10,8,8));
		CitzensPanel.setBackground(Color.DARK_GRAY);
		CitzensPanel.setPreferredSize(new Dimension(50,100));
		mainPanel.add(CitzensPanel,BorderLayout.SOUTH);
	}
	
	public void createWorldPanel() {
		WorldPanel = new JPanel();
		WorldPanel.setLayout(new GridLayout(10,10));
		WorldPanel.setBorder(BorderFactory.createEmptyBorder(10,10,8,8));
		WorldPanel.setBackground(Color.DARK_GRAY);
		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				JButton button= new JButton();
				button.setPreferredSize(new Dimension(90,90));
				World[i][j] = button;
				WorldPanel.add(World[i][j]);
			}
		}
		mainPanel.add(WorldPanel, BorderLayout.CENTER);
	}
	
	public void createHelp() {
		Help = new JButton("Help");
		FunctionPanel.add(Help);
		Help.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					commandCenter.Recommend();
					commandCenter.getEngine().nextCycle();
					currentCycle++;
					labelCurrentCycle.setText("Current Cycle:" + " " + currentCycle);
					labelcasualties.setText("Number of casualties:" + " " + commandCenter.getEngine().calculateCasualties());
					updateDead();
					updateWorld();
					updateDisaster();
					updateUnits();
					GameOver();
				} catch (IncompatibleTargetException | CannotTreatException | CitizenAlreadyDeadException | BuildingAlreadyCollapsedException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		});
	}
	
	public void createInfo(){
		JPanel InfoPanel = new JPanel();
		InfoPanel.setPreferredSize(new Dimension(400,50));
		InfoPanel.setLayout(new BorderLayout());
		GameData = new JPanel();
		GameData.setPreferredSize(new Dimension(50,50));
		GameData.setLayout(new BorderLayout());
		InfoPanel.add(GameData,BorderLayout.SOUTH);
		Info = new TextArea();
		Info.setPreferredSize(new Dimension(600,300));
		Info.setEditable(false);
		Info.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		Info.setBackground(Color.WHITE);
		TActiveDisaster = new TextArea();
		TActiveDisaster.setPreferredSize(new Dimension(190,300));
		TActiveDisaster.setEditable(false);
		TActiveDisaster.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		TActiveDisaster.setBackground(Color.WHITE);
		TExecutedDisaster = new TextArea();
		TExecutedDisaster.setPreferredSize(new Dimension(190,300));
		TExecutedDisaster.setEditable(false);
		TExecutedDisaster.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		TExecutedDisaster.setBackground(Color.WHITE);
		InfoPanel.add(Info,BorderLayout.NORTH);
		FunctionPanel = new JPanel();
		//FunctionPanel.setBackground(Color.cyan);
		FunctionPanel.setPreferredSize(new Dimension(600,600));
		FunctionPanel.setLayout(new GridLayout(4,0));
		Dead = new JPanel();
		Dead.setPreferredSize(new Dimension(20,20));
		Dead.setLayout(new FlowLayout());
		Dead.setBackground(Color.DARK_GRAY);
		JScrollPane ScrollDead = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		ScrollDead.setViewportView(Dead);
		FunctionPanel.add(ScrollDead);
		FunctionPanel.add(TExecutedDisaster);
		FunctionPanel.add(TActiveDisaster);
		createRespondButton();
		InfoPanel.add(FunctionPanel,BorderLayout.CENTER);
		mainPanel.add(InfoPanel, BorderLayout.WEST);
	}
	
	public void createRespondButton() {
		ButtonRespond = new JButton("Respond");
		FunctionPanel.add(ButtonRespond);
		ButtonRespond.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
						if(rescuable instanceof Citizen)
							selectedUnit.respond((Citizen)rescuable);
						else
							selectedUnit.respond((ResidentialBuilding)rescuable);
					selectedUnit = null;
					rescuable= null;
				} catch (CannotTreatException | IncompatibleTargetException | NullPointerException e) {
					if(e instanceof NullPointerException)
						JOptionPane.showMessageDialog(null,"Please Make Sure you have selected a Unit and Target");
					else
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		});
	}
	
	public void createUnitsPanel(JPanel UnitsPanel) {
		UnitsPanel.setLayout(new BorderLayout(8,8));
		UnitsPanel.setBorder(BorderFactory.createEmptyBorder(10,0,8,0));
		UnitsPanel.setPreferredSize(new Dimension(400,getHeight()));
		JPanel AUnitsPanel = new JPanel();
		createAUnitsPanel(AUnitsPanel,UnitsPanel);
		JPanel RUnitsPanel = new JPanel();
		createRUnitsPanel(RUnitsPanel,UnitsPanel);
		JPanel TUnitsPanel = new JPanel();
		createTUnitsPanel(TUnitsPanel,UnitsPanel);
		mainPanel.add(UnitsPanel,BorderLayout.EAST);
	}
	
	public void createAUnitsPanel(JPanel AUnitsPanel,JPanel UnitsPanel) {
		AUnitsPanel = new JPanel();
		AUnitsPanel.setLayout(new BorderLayout());
		AUnitsPanel.setPreferredSize(new Dimension(UnitsPanel.getWidth(),300));
		JLabel label= new JLabel("Available Units", JLabel.CENTER);
		label.setPreferredSize(new Dimension(500,30));
		label.setBackground(Color.LIGHT_GRAY);
		label.setOpaque(true);
		AUnitsPanel.add(label,BorderLayout.NORTH);
		AvailableUnitsPanel.setPreferredSize(new Dimension(AUnitsPanel.getWidth(),250));
		AvailableUnitsPanel.setLayout(new GridLayout(1,0));
		ArrayList<Unit> Units = commandCenter.getEmergencyUnits();
		for(int i=0;i<Units.size();i++) {
			Unit u = Units.get(i);
			ImageIcon image;
			Image img;
			Image dimg;
			if(u instanceof FireTruck) {
				 UnitBT = new UnitButton((FireTruck)u);
				 //UnitBT.setPreferredSize(new Dimension(80,250));
				image = new ImageIcon("FireTruck.png");
				img = image.getImage();
				dimg = img.getScaledInstance(80,250,Image.SCALE_SMOOTH);
				image.setImage(dimg);
				UnitBT.setIcon(image);
				UnitBT.setToolTipText("FireTruck");
				AvailableUnitsPanel.add(UnitBT);
				AvailableUnits.add(UnitBT);
			}
			if(Units.get(i) instanceof Ambulance) {
				UnitBT = new UnitButton((Ambulance)u);
				UnitBT.setPreferredSize(new Dimension(80,250));
				image = new ImageIcon("AMB.png");
				img = image.getImage();
				dimg = img.getScaledInstance(80,250,Image.SCALE_SMOOTH);
				image.setImage(dimg);
				UnitBT.setIcon(image);
				UnitBT.setToolTipText("Ambulance");
				AvailableUnitsPanel.add(UnitBT);
				AvailableUnits.add(UnitBT);
			}
			if(Units.get(i) instanceof Evacuator) {
				UnitBT = new UnitButton((Evacuator)u);
				UnitBT.setPreferredSize(new Dimension(80,250));
				image = new ImageIcon("police.png");
				img = image.getImage();
				dimg = img.getScaledInstance(80,250,Image.SCALE_SMOOTH);
				image.setImage(dimg);
				UnitBT.setIcon(image);
				UnitBT.setToolTipText("Evacuator");
				AvailableUnitsPanel.add(UnitBT);
				AvailableUnits.add(UnitBT);
			}
			if(Units.get(i) instanceof DiseaseControlUnit) {
				UnitBT = new UnitButton((DiseaseControlUnit)u);
				UnitBT.setPreferredSize(new Dimension(80,250));
				image = new ImageIcon("DC.jpeg");
				img = image.getImage();
				dimg = img.getScaledInstance(80,250,Image.SCALE_SMOOTH);
				image.setImage(dimg);
				UnitBT.setIcon(image);
				UnitBT.setToolTipText("DiseaseControlUnit");
				AvailableUnitsPanel.add(UnitBT);
				AvailableUnits.add(UnitBT);
			}
			if(Units.get(i) instanceof GasControlUnit) {
				UnitBT = new UnitButton((GasControlUnit)u);
				UnitBT.setPreferredSize(new Dimension(80,250));
				image = new ImageIcon("truck.jpeg");
				img = image.getImage();
				dimg = img.getScaledInstance(80,250,Image.SCALE_SMOOTH);
				image.setImage(dimg);
				UnitBT.setIcon(image);
				UnitBT.setToolTipText("GasControlUnit");
				AvailableUnitsPanel.add(UnitBT);
				AvailableUnits.add(UnitBT);
			}
			UnitBT.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					selectedUnit = u;
					Info.setText(u.toString());
				}
			});
		}
		AUnitsPanel.add(AvailableUnitsPanel);
		UnitsPanel.add(AUnitsPanel,BorderLayout.NORTH);
	}
	
	public void createRUnitsPanel(JPanel RUnitsPanel,JPanel UnitsPanel) {
		RUnitsPanel = new JPanel();
		RUnitsPanel.setLayout(new BorderLayout());
		RUnitsPanel.setPreferredSize(new Dimension(UnitsPanel.getWidth(),300));
		JLabel label= new JLabel("Responding Units",JLabel.CENTER);
		label.setPreferredSize(new Dimension(500,30));
		label.setBackground(Color.LIGHT_GRAY);
		label.setOpaque(true);
		RUnitsPanel.add(label,BorderLayout.NORTH);
		RespondingUnitsPanel.setLayout(new GridLayout(1,0));
		RespondingUnitsPanel.setPreferredSize(new Dimension(RUnitsPanel.getWidth(),250));
		RUnitsPanel.add(RespondingUnitsPanel);
		UnitsPanel.add(RUnitsPanel,BorderLayout.CENTER);
	}
	
	public void createTUnitsPanel(JPanel TUnitsPanel,JPanel UnitsPanel) {
		TUnitsPanel = new JPanel();
		TUnitsPanel.setLayout(new BorderLayout());
		TUnitsPanel.setPreferredSize(new Dimension(UnitsPanel.getWidth(),300));
		JLabel label= new JLabel("Treating Units",JLabel.CENTER);
		label.setPreferredSize(new Dimension(500,30));
		label.setBackground(Color.LIGHT_GRAY);
		label.setOpaque(true);
		TUnitsPanel.add(label,BorderLayout.NORTH);
		TreatingUnitsPanel.setLayout(new GridLayout(1,0));
		TreatingUnitsPanel.setPreferredSize(new Dimension(TUnitsPanel.getWidth(),250));
		TUnitsPanel.add(TreatingUnitsPanel);
		UnitsPanel.add(TUnitsPanel,BorderLayout.SOUTH);
	}
	
	public void updateUnits() {
		AvailableUnitsPanel.removeAll();
		RespondingUnitsPanel.removeAll();
		TreatingUnitsPanel.removeAll();
		for(int j=0;j<AvailableUnits.size();j++) {
			Unit unit = AvailableUnits.get(j).getUnit();
			for(int i=0;i<commandCenter.getEmergencyUnits().size();i++) {
				if(commandCenter.getEmergencyUnits().get(i).equals(unit)) {
					if(commandCenter.getEmergencyUnits().get(i).getState()==UnitState.IDLE)
						AvailableUnitsPanel.add(AvailableUnits.get(j));
					if(commandCenter.getEmergencyUnits().get(i).getState()==UnitState.RESPONDING)
						RespondingUnitsPanel.add(AvailableUnits.get(j));
					if(commandCenter.getEmergencyUnits().get(i).getState()==UnitState.TREATING)
						TreatingUnitsPanel.add(AvailableUnits.get(j));
				}
			}
		}
		AvailableUnitsPanel.updateUI();
		RespondingUnitsPanel.updateUI();
		TreatingUnitsPanel.updateUI();
		AvailableUnitsPanel.validate();
		RespondingUnitsPanel.validate();
		TreatingUnitsPanel.validate();
	}
	
	public void GameOver(){
		if(commandCenter.getEngine().checkGameOver()==true) {
			JOptionPane.showMessageDialog(null,"Score :" +  " "  + commandCenter.getEngine().calculateCasualties());
			dispose();
			new GameOverGUI();
		}
	}
	
 	public void NextCycleUpdate() {
		labelCurrentCycle=new JLabel("Current Cycle:" +  " "  + currentCycle,JLabel.CENTER);
		GameData.add(labelCurrentCycle,BorderLayout.NORTH);
		ButtonNextCycle=new JButton("NextCycle");
		ButtonNextCycle.setPreferredSize(new Dimension(100,100));
		FunctionPanel.add(ButtonNextCycle);
		labelcasualties = new JLabel("Number of casualties:" +  " "  + commandCenter.getEngine().calculateCasualties(),JLabel.CENTER);
		GameData.add(labelcasualties,BorderLayout.SOUTH);
		ButtonNextCycle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
						try {
							commandCenter.getEngine().nextCycle();
							currentCycle++;
							labelCurrentCycle.setText("Current Cycle:" + " " + currentCycle);
							labelcasualties.setText("Number of casualties:" + " " + commandCenter.getEngine().calculateCasualties());
							updateDead();
							updateWorld();
							updateDisaster();
							updateUnits();
							GameOver();
						} catch (CitizenAlreadyDeadException | BuildingAlreadyCollapsedException
								| CannotTreatException e) {
							JOptionPane.showMessageDialog(null, e.getMessage());
						}
			}
		});
	}
	
 	public void updateDisaster() {
 		ArrayList<Disaster> Excueted = commandCenter.getEngine().getExecutedDisasters();
 		ArrayList<Citizen> citizens = commandCenter.getVisibleCitizens();
 		ArrayList<ResidentialBuilding> building = commandCenter.getVisibleBuildings();
 		String E = "---Excueted Disaster---- \n";
 		for(int i=0;i<Excueted.size();i++) {
 				E+=Excueted.get(i) + "\n";
 			}
 		TExecutedDisaster.setText(E);
 		TExecutedDisaster.validate();
 		String A = "---Active Disaster---- \n";
 		for(int i=0;i<citizens.size();i++) {
 			A += citizens.get(i).getDisaster() + "\n";
 		}
 		for(int i=0;i<building.size();i++) {
 			A += building.get(i).getDisaster() + "\n";
 		}
 		TActiveDisaster.setText(A);
 		TActiveDisaster.validate();
 	}
 	
 	public void updateDead() {
 		Dead.removeAll();
 		for(int i=0;i<commandCenter.getVisibleCitizens().size();i++) {
 			Citizen citizen = commandCenter.getVisibleCitizens().get(i);
 			if(citizen.getState()==CitizenState.DECEASED) {
 				JButton dead = new JButton(citizen.getName());
 				dead.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						Info.setText(citizen.toString());
					}
				});
 				Dead.add(dead);
 			}
 		}
 		for(int j =0;j<commandCenter.getVisibleBuildings().size();j++) {
 			for(int i=0;i<commandCenter.getVisibleBuildings().get(j).getOccupants().size();i++) {
 				Citizen citizen = commandCenter.getVisibleBuildings().get(j).getOccupants().get(i);
 				if(citizen.getState()==CitizenState.DECEASED) {
 	 				JButton dead = new JButton(citizen.getName());
 	 				dead.addActionListener(new ActionListener() {
 						
 						@Override
 						public void actionPerformed(ActionEvent arg0) {
 							Info.setText(citizen.toString());
 						}
 					});
 	 				Dead.add(dead);
 	 			}
 			}
 		}
 	}
 	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub	
	}
	
	public CommandCenter getCommandCenter() {
		return commandCenter;
	}

	public static void main (String [] args) throws Exception {
		new View();
		}
}
