# Brainflow #

## Overview ##

**_Brainflow_** is a desktop application for the visualization of functional brain imaging data.  The goal of **_Brainflow_** is to make the display of volumetric imaging data as painless and easy as possible.  Certain things, such as viewing multiple image layers, creating custom color maps, performing 'on the fly' conjunction analyses, are not terribly well supported by existing visualization packages. Brainflow attempts to remedy this.

### Goals of the Project ###

**_Brainflow_** has relatively limited aims.  Like MRICro http://www.sph.sc.edu/comd/rorden/mricro.html Brainflow is a _visualization_ not an _analysis_ tool.  Brainflow will not run statistical analyses on neuroimaging data and it will not do very much in the way of complex image processing.  Very good tools for such things exist, for instance AFNI http://afni.nimh.nih.gov/afni/, SPM http://www.fil.ion.ucl.ac.uk/spm/ and FSL http://www.fmrib.ox.ac.uk/fsl/.  Brainflow will however, happily read in images (in standard brain imaging formats, e.g. NIFTI, Analyze, AFNI) that are created by such packages. Indeed, that is the point of Brainflow -- to visualize the results of statistical analyses carried out with other tools.

### Cross Platform ###

Brainflow is written in the Java programming languange.  It should run on windows, linux, Mac and other platforms that support the Java runtime.  This will of course require that you have (the latest version, JAVA 1.6.0) installed on your computer.






