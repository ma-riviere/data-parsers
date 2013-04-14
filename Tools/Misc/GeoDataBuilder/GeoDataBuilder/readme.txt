
Usage: GeoDataBuilder <options>
 -c PATH     : Path to Aion client installation. Required
 -s PATH     : Path to Aion server installation (Aion Extreme Emu 2.5). Required
 -lvl LVLID  : Set exect level Id e.g. 110010000
 -no_h32     : Do not include *.h32 data into *.geo file
 -no_mesh    : Do not generate mesh.geo file
 -t PATH     : Path to the temporary folder. If not set ./tmp folder will be used
 -v VER      : Version Aion client installation (1 or 2). Default is 2
 -w PATH     : Path to WorldId.xml 
 -no_cleanup : Do not delete unpacked, converted, etc. files (Not implemented yet)
 
Steps of looking for WorldId.xml:
 
 1. If parameter -w <path> is set then trying to use file at that path.
 2. If WorldId.xml was not found at previous step then try to extract this file from Aion's World.pak
 3. Otherwise try to use WorldId.xml from current folder.

If -lvl command line parameter is set then WorldId.xml file is not used.  
 
 Conversion of Pak files partially taken from pak2zip.py
 Decoding of binary Xml taken fully from Aion Extractor
 Cgf format - http://sourceforge.net/projects/pyffi/files/
 Format of the *.geo and meshs.geo taken from geoEngine-0.1.jar
 