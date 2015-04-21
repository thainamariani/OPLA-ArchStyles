###Dependencies

This project is a module of OPLA-Tool, which is needed to make OPLA-ArchStyles works. You must download the OPLA-Tool project at https://github.com/edipofederle/architecture-representation.git.

---------------------------------------------------------------------------------------------------------------------

###Organization of Packages

**config, templates and perfis:** have some files needed for OPLA-Tool to work;

**lib:** has the needed jars;

**test:** has the project tests;

**src:** this package is divided in the following packages:

* **project:** has all the essential classes of the project;
* **gui:** at the time, there is no graphic interface to support OPLA-ArchStyles. That way, this package contains some support classes with static information to do, for now, the graphic interface role;
* **experiment:** has some support classes to execute the experiments;
* **results:** has some support classes to calculate the results.

-------------------------------------------------------------------------------------------------------------------

###Running

For running, you must pass to the experiment class (src.experiment.Experiment), respectively, the following information as parameters:

* Population size (example: 100);
* Max Evaluations (example: 10000);
* Mutation Probability (example: 0.9);
* Absolute PLA path (example: /home/plas/agm/agm.uml);
* The architectural style to be used. Such value must be *layer*, *clientserver*, *aspect*, or any word if you do not want to use styles (example: layer).
* The folder to be saved (example: test).

If you want to preserve aspects in addition to the architectural style given as parameter, then the boolean parameter *aspect* in the Experiment class must be true. Otherwise, such value must be *false*. If the architectural style given as parameter is *aspect*, this boolean value is unnecessary and ignored.

After that, the architectural style informed is validate in the PLA. If it is not designed correctly, a message is showed as output. Otherwise, the PLA is optimized.

-------------------------------------------------------------------------------------------------------------------


For more information visit https://github.com/thainamariani/OPLA-ArchStyles/blob/master/Information.pdf
