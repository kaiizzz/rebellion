# rebellion
model of netlogo model "rebellion" in java

## INSTRUCTIONS ON HOW TO RUN THE MODEL
- Make sure you have program that can run Java

### Parameters. 
To change the parameters of the model, you can change the values of the following variables in the `Params.java` file:

- MAP_SIZE: the size of the map, set to 40 by default as seen in the original model
- VISION: the vision of the agents and police, set to 7 by default as seen in the original model
- MAX_JAIL_TERM: the maximum jail term for agents, set to 30 by default as seen in the original model
- INITIAL_AGENT_DENSITY: the initial density of agents in the map, set to 70 by default as seen in the original model
- INITIAL_POLICE_DENSITY: the initial density of police in the map, set to 4 by default as seen in the original model
- GOVERNMENT_LEGITIMACY: the perceived legitimacy of the central authority, set to 0.82 by default as seen in the original model
- EXTENTSION_SCALING: the proportion government legitimacy increases for each nearby jailed agents when extension is enabled, set to 0.003 by default.
- MOVEMENT: setting it ture will make all entities move, false, they will not move. Set to true by default.

- TICK: the speed of simulation (higher number is slower), Set this parameter if you want to use the map GUI feature.

### Running the model
To run the model, you can navigate to the `Main.java` file and run the file.

You will be prompted in the console to enter if you want to run the normal or extended model. Enter `y` for the extended model and `n` for the normal model in the command line.

You will then be prompted to enter the number of steps you want to run the model for. Enter the number of steps (integer above 0) you want to run the model for in the command line.

The model will then ask for how many runs you want to run the model for (So you can do multiple simulations with the same parameters per dun of the program). Enter the number of runs (integer above 0) you want to run the model for in the command line.

The model will then ask you if you want the GUI to be displayed. Enter `y` for the GUI to be displayed and `n` for the GUI not to be displayed in the command line. This feature may not work with some IDEs. This feature does not influence the results of the model.

The model will then run for the number of steps you entered times by the number of runs you entered. The model will then outputs the result into a file called `output.csv` in the root directory of the project. Note: if you run the model multiple times, the output will be appended to the file. 

### Output
The output of the model is a csv file called `output.csv` in the root directory of the project. The output contains the following columns for each run:
step,quiet agents,jailed agents,active agents

At the top of the output file after the right most run, you can find the average stats across all runs (mean of run means, mean of run variances) for each agent type

The command line will prompt you when the simulation has completed and when the stats have been written to the csv file.


.▀█▀.█▄█.█▀█.█▄.█.█▄▀　█▄█.█▀█.█─█
─.█.─█▀█.█▀█.█.▀█.█▀▄　─█.─█▄█.█▄█


