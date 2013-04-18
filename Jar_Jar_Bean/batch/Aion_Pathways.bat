@ECHO OFF

:: ########### INPUT ###############

:: Data
SET I_TOYPETS=%CLIENT%\Data\func_pet\toypet*.xml
SET I_ANIMATIONS=%CLIENT%\Data\Animations\custom_animation.xml
SET I_COOLTIMES=%CLIENT%\Data\world\client_instance_cooltime*.xml
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
SET DATA_STRINGS=%CLIENT%\Data\Strings\client_strings*.xml

:: Levels
SET LEVELS=E:\Jeux\Aion\Client\Client\aion40\Levels\

:: L10N
SET L10N_STRINGS=%CLIENT%\L10N\ENU\data\strings\client_strings*.xml
SET QUEST_DIALOGS=%CLIENT%\L10N\ENU\data\dialogs\quest*.xml

:: ########### OUTPUT ##############

:: Server
SET O_TOYPETS=%SERVER%\toypets\*.xml
SET O_COOLTIMES=%SERVER%\instance_cooltimes.xml
SET O_SPAWNS=%SERVER%\Spawns\
SET O_ITEMS=%SERVER%\item_templates*.xml
SET O_WORLD_MAPS=%SERVER%\world_maps.xml
SET O_RIDES=%SERVER%\ride.xml
SET O_COOLTIMES=%SERVER%\instance_cooltimes.xml
SET O_SKILLS=%SERVER%\skills\skill_templates.xml
SET O_SKILL_TREE=%SERVER%\skills\skill_tree.xml
SET O_RECIPES=%SERVER%\recipe_templates.xml
SET O_NPCS=%SERVER%\npc_templates.xml
SET O_HOUSING=%SERVER%\housing\*.xml
SET O_TOYPETS=%SERVER%\toypets\*.xml
SET O_WALKERS=%SERVER%\npcs\npc_walker*.xml

:: Custom
SET SOURCE_SPHERE=%CUSTOM%\source_sphere.xml
SET HEIGHT_MAP=%CUSTOM%\height_map.xml
SET ITEMS_NAME=%CUSTOM%\items_name_id.xml