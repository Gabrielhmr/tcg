Test Case Generation - TCG
=========

1. About TCG
------------
TCG is a plugin for the generation and selection of test cases for functional and statistical tests that accepts as input both probabilistic and non-probabilistic models. The test case selection techniques provided are test purposes, random path and most probable path, and a new selection technique called minimum probability of path in which the tool selects test cases that have probability greater than or equal to a value provided by the user.

2. Using TCG - Steps
--------------------
The TCG tool (Test Case Generation) is a plugin for LoTuS modeling tool. LoTuS is a open-source tool to graphic behaviour modelling of software using LTS.

  Step 1: Download the lotus tool - http://jeri.larces.uece.br/lotus ;
  
  Step 2: Add o TCG plugin [tcg.jar] in the Extensions folder that is located in the root directory of the LoTuS tool;
  
  Step 3: Just launch the Lotus application (running java -jar lotus.jar) and TCG plugin will automatically load ;

*LoTuS Github: https://github.com/lotus-tool

3. Interface TCG
----------------
The plugin appears visually in the tool as the item "TCG" from the main menu. Where are found three sub-menus: Functional, Statistical and About. The functional contains the criteria for the generation and selection of cases of non-probabilistic tests and Statistical contains the criteria for the generation and selection of cases of probabilistic tests.

Clicking the Statistical Functional or sub-menu to tool opens a new tab containing title as the project name - [TCG], by clicking on the tab it is possible to view the formal model and the settings panel.


4. Getting Started TCG
----------------------
The TCG provides the generation and selection of functional test cases (non-probabilistic) and statistical (probabilistic). The plugin consists of two phases: setup phase and the implementation phase. The configuration phase (in the Configuration tab), which enables the user to select and configure four steps:

  Step 1: Select and configure the generator - the user must select a generation technique, which can be: all        free-loop paths, all one-loop paths, all paths, all states, all transitions, random path, random probabilitic      path ou shortest path.

  Step 2: Adjust the testing purposes (optional) - the test purpose (test purpose) is optional. If the user does     not determine the test purpose, will be used the expression * & ACCEPT will return all possible paths to be taken   from the model.

  Step 3:  Select and configure the seletor - the user must select a selection technique, which can be: minimum      probability of path, most probable path, similarity of paths, weighted similarity of paths and/or test purpose

  Step 4: Set stop condition (optional) - the stop condition (stop condition) is optional. The user can set a timer   to stop condition, aiming to avoid the infinite execution of test cases.

Upon completion of the above steps, the process of generating the test cases can be started. The result of the setting step is equivalent to a configuration package and is passed to the execution stage. 

In the implementation phase we use the generation algorithm chosen along with the test purpose, if one has been specified, to generate test cases. The generation will end when the end algorithm (normal running), or when a stop condition added by the user is satisfied, or if the generation is aborted directly by the user (via the stop button).

After the test cases are generated, the chosen selection algorithm is applied and the tests are filtered according to the selected criteria selection algorithm, thus resulting in a subset of the test cases generated. This subset of results will be displayed on the Results tab.

5. More Information
-------------------

Articles: 

	- TCG: A Model-based Testing Tool for Functional and Statistical Testing:

http://www.larces.uece.br/~gesad/wp-content/uploads/2015/04/iceis2015_tcg.pdf
	
	- LoTuS-TCG: Uma ferramenta para geração e seleção de casos de teste Funcionais e Estatísticos:

http://www.larces.uece.br/~gesad/wp-content/uploads/2015/04/sast2014.pdf

Monograph:

	- TCG: Uma ferramenta para geração e seleção de casos de testes Funcionais e Estatísticos:

http://www.larces.uece.br/~gesad/wp-content/uploads/2015/08/Monografia-Especializa%C3%A7%C3%A3o-Laryssa-Lima-Muniz.pdf
