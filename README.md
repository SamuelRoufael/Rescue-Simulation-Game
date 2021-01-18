# Rescue-Simulation-Game

Intro
The Rescue Simulation is based on concepts of the RoboCup, an annual international robotics
competition proposed and founded in 1996 by a group of university professors. The GUC has
been taking part in this competition for few years and won various titles including the best team.
The aim of rescue simulations is to derive strategies and behaviors for maximizing the number
of saved lives and minimizing the number of casualties. The hope is that one day these
strategies can be applied by actual policemen, firefighters and medics in real life disasters and
accidents.
Simulation Game Rules
The game consists of a main simulation 10x10 grid where the whole map is taking place.
The game is a single player one, where the player is responsible for managing the rescue units
to handle buildings and citizens suffering from the disasters that ensue during gameplay
throughout the whole map. The main goal of the game is for the player to efficiently employ the
rescue units to rescue as many citizens and minimize the number of deaths. All the available
rescue units are assigned to the player at the start of the simulation. As the game proceeds
certain disasters start appearing throughout the map affecting different buildings and citizens.
Winning Conditions
The game ends when all the planned disasters are either handled by rescuing the targeted
citizens or failing to do so, thus the death of these citizens. When all citizens are either rescued
or deceased the simulation game ends and the score of saves and losses is presented to the
player.
Cycles
The game is cycle based meaning that time is represented as cycles passing within the
simulation. Each rescue task takes a specific number of cycles and each disaster reaches its full
effect in a specific number of cycles. The player controls the progression of the cycles.
Simulation Components
There are different components in the game, each with special abilities, weaknesses, and goals.
The game has a grid-based map representing the area covered in the rescue simulation. The
map is occupied by buildings and citizens residing in said buildings. There are different
disasters that can affect both buildings and citizens in different ways. Accordingly, there are
different rescue units to be controlled by the player to handle said disasters.
Buildings
Buildings have citizen occupants. Each building has its own address i.e. locations on the map,
that is different from the other buildings. At any given cycle in the simulation, a building can
suffer from a disaster. This is why building disasters need rescue units to tend to the building
itself and in some cases its citizens as well. Buildings have structural integrities representing the
status of the building. The damage caused by fire disasters and the foundation damage caused
by collapse, decreases the structural integrity of a building.
Citizens
Citizens are the people occupying the map. They can be located anywhere in the map including
any of the residential buildings . Each citizen has health points (hp) indicating the current health.
Citizens also have blood loss, toxicity indicators which are affected by being in a disaster.
Citizens have different states that indicate the status of the current simulation scenario. At any
given cycle in the game a citizen can either be safe i.e. not suffering from any disasters, in
trouble which means a disaster is currently directly affecting this citizen, rescued from the
disaster or deceased due to said disaster.
Disasters
There are different types of disasters that can occur throughout the game. Some of them affect
buildings while others directly affect the citizens in the map. Buildings can 1) catch fire, 2)
collapse or 3) have a gas leak. Citizens can 4) get infected or 5) injured.
Fire Disasters:
Fire disasters can affect buildings. It will increase the fire damage of the affected buildings. If the
fire damage of a building reaches 100, it will cause said building to collapse. Moreover, if a fire
disaster befalls a building that has a gas leak, the building will collapse if the gas level is low. If
the gas level is high when the fire disaster hits however, the building will be destroyed and all
the occupants will die immediately.
Collapse Disasters:
Collapse disasters will automatically trigger once the fire damage of any building reaches or
surpasses 100. Once initiated, it will keep on increasing the foundation damage of the building.
If the foundation damage reaches 100, the building will be destroyed and all the occupants will
die immediately !! The player does not have any way of stopping the increase of the foundation
damage of a collapsed building.
Gas Leak Disasters:
If a building got some gas leak, it will increase the gas level inside this building which is very
dangerous for the occupants. If the level is not dealt with before it reaches 100, all the
occupants will suffocate and die horribly !! If a gas leak disaster hits a building that is currently
affected by a fire disaster, the building will collapse.
Infection Disasters:
Citizens can get infected. This will cause their toxicity level to increase which if not controlled in
the right time and is left to reach 100, will cause the citizen to die immediately !!
Injury Disasters:
Citizens can also get injured. If so, this will cause them to lose some blood. Like the infection
disasters, if it is not dealt with before the amount of blood loss reaches 100, the citizen will
awfully die !!
Emergency Units
There are three types of emergency units that can tend to buildings or citizens suffering from
disaster, namely the police units, the fire units, and the medical units.
Emergency units have different states that indicate the status of the current simulation scenario.
At any given cycle in the game an emergency unit can have one of the following states:
● Idle: waiting for dispatch call and not assigned to any disaster.
● responding: assigned to a specific disaster and enroute to respond to it.
● Treating: currently treating/handling a specific citizen/building.
Police Units
Police units are responsible for maintaining public order and safety.
There is only one type of police units, which is the Evacuator. Evacuators are responsible for
rescuing citizens from collapsed buildings by transporting them from a collapsing building to a
safe zone.
Medical Units :
Medical units are responsible for dealing with health-related disasters and also restoring the
health of the affected citizens after their disasters have been controlled.
There are two types of medical units: the ambulance and the disease control unit.
Ambulances are responsible for rescuing and healing citizens with injury disasters. The disease
control units are used to rescue and heal citizens with infection disasters.
Fire Units
Fire Units are responsible for dealing with fire and gas disasters.
There are two types of fire units: the fire truck and the gas control unit.
The fire trucks are responsible for putting out fires in buildings suffering from fire disasters. The
gas control unit handles gas leaks in buildings.
Disaster - Effect Matrix
Fire Collapse Gas Leak Injury Infection
Building Fire damage ++ Foundation
damage ++
Gas level ++ N/A N/A
Citizen N/A N/A N/A blood loss ++ Toxicity ++
Control Panels
The game consists of various panels:
1. The rescue panel which contains the map showing the status of the world and the
buildings and citizens in it. Each cell of the map is either empty or contains a building or
a citizen, each of which can either be safe or currently suffering from a disaster.
2. The units panel which shows the status of all the units available to the player.
3. The info panel, where the player can see information about any of the simulation
components i.e. buildings or citizens as well as about the ongoing events i.e. disasters
and how they are being controlled.
