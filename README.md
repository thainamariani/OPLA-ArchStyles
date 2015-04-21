###Dependencies

This project is a module of OPLA-Tool, which is needed to make OPLA-ArchStyles works. You must download the OPLA-Tool project at https://github.com/edipofederle/architecture-representation.git.

---------------------------------------------------------------------------------------------------------------------

###Organization of Packages

**config, templates and perfis:** have some files needed to OPLA-Tool works;

**lib:** has the needed jars;

**test:** has the project tests;

**src:** this package is divided in the following packages:

* **project:** has all the essential classes of the project;
* **gui:**: at the time, there is no graphic interface to support OPLA-ArchStyles. That way, this package contains some support classes with static information to do, for now, the graphic interface role;
* **experiment:** has some support classes to execute the experiments;
* **results:** has some support classes to calculate the results.
