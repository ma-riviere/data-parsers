@ECHO OFF

REM ########### INPUT ###############

REM ########### OUTPUT ##############
SET I_TOYPETS=%CLIENT%\Data\func_pet\toypet*.xml
SET O_TOYPETS=%SERVER%\toypets\*.xml
SET ANIMATIONS=%CLIENT%\Data\Animations\custom_animation.xml
SET I_COOLTIMES=%CLIENT%\Data\world\client_instance_cooltime*.xml
SET O_COOLTIMES=%SERVER%\instance_cooltimes.xml

SET LEVELS=%CLIENT%\Levels\
SET SPAWNS=%SERVER%\spawns\

SET DATA_STRINGS=%CLIENT%\Data\Strings\client_strings*.xml
SET L10N_STRINGS=%CLIENT%\L10N\ENU\data\strings\client_strings*.xml
SET I_WORLD_MAPS=%CLIENT%\Data\world\WorldId.xml
SET I_WORLD_DATA=%CLIENT%\Data\world\client_world_*.xml
SET I_RIDES=%CLIENT%\Data\rides\rides.xml
SET I_COOLTIMES=%CLIENT%\Data\world\client_instance_cooltime*.xml
SET I_ITEMS=%CLIENT%\Data\Items\client_items_*.xml
SET I_RECIPES=%CLIENT%\Data\Items\client_combine_recipe.xml
SET I_SKILLS=%CLIENT%\Data\skills\client_skills.xml
SET I_SKILL_TREE=%CLIENT%\Data\skills\client_skill_learns.xml
SET I_NPCS=%CLIENT%\Data\Npcs\client_npcs.xml
SET I_HOUSING=%CLIENT%\Data\Housing\client_housing*.xml

SET SOURCE_SPHERE=%CUSTOM%\source_sphere.xml
SET HEIGHT_MAP=%CUSTOM%\height_map.xml
SET ITEMS_NAME=%CUSTOM%\items_name_id.xml

REM SET QUEST_DIALOGS=%CLIENT%\L10N\ENU\data\dialogs\quest*.xml