package com.parser.write.aion.items;


import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.parser.input.aion.items.*;

import com.parser.common.aion.AionDataMerger;
import com.parser.read.aion.AionReadingConfig;
import com.parser.read.aion.items.AionItemsParser;
import com.parser.write.AbstractWriter;
import com.parser.write.FileMarhshaller;
import com.parser.write.aion.AionWritingConfig;

public class AionClientItemsWriter extends AbstractWriter {

	ClientItems finalTemplates = new ClientItems();
	Collection<ClientItem> templateList = finalTemplates.getClientItem();
	Map<String, List<ClientItem>> clientItemData;
	Map<String, List<ClientItem>> customClientItemData;
	List<ClientItem> mergedClientItemList = new ArrayList<ClientItem>();
	
	@Override
	public void parse() {
		clientItemData = new AionItemsParser().parse();
		if (AionReadingConfig.READ_CUSTOM)
			customClientItemData = new AionItemsParser(AionReadingConfig.ITEMS_BINDINGS, AionReadingConfig.ITEMS_CUSTOM, AionReadingConfig.ITEMS_PREFIX).parse();
		if (AionWritingConfig.WRITE_CUSTOM)
			mergedClientItemList = new AionDataMerger().mergeItemData(clientItemData, customClientItemData);
	}

	@Override
	public void transform() {
		if (mergedClientItemList != null && !mergedClientItemList.isEmpty()) {
			for (ClientItem from : mergedClientItemList) {
				ClientItem to = new ClientItem();
				// START
				to.setId(from.getId());
				to.setName(from.getName());
				to.setDesc(from.getDesc());
				to.setArmorType(from.getArmorType());
				to.setItemType(from.getItemType());
				to.setMaterial(from.getMaterial());
				to.setIconName(from.getIconName());
				to.setPrice(from.getPrice());
				to.setMaxStackCount(from.getMaxStackCount());
				to.setEquipmentSlots(from.getEquipmentSlots());
				to.setDodge(from.getDodge());
				to.setQuality(from.getQuality());
				to.setLevel(from.getLevel());
				to.setMagicalResist(from.getMagicalResist());
				to.setPhysicalDefend(from.getPhysicalDefend());
				to.setLore(from.getLore());
				to.setCanExchange(from.getCanExchange());
				to.setCanSellToNpc(from.getCanSellToNpc());
				to.setCanDepositToCharacterWarehouse(from.getCanDepositToCharacterWarehouse());
				to.setCanDepositToAccountWarehouse(from.getCanDepositToAccountWarehouse());
				to.setCanDepositToGuildWarehouse(from.getCanDepositToGuildWarehouse());
				to.setBreakable(from.getBreakable());
				to.setSoulBind(from.getSoulBind());
				to.setRemoveWhenLogout(from.getRemoveWhenLogout());
				to.setGenderPermitted(from.getGenderPermitted());
				to.setWarrior(from.getWarrior());
				to.setScout(from.getScout());
				to.setMage(from.getMage());
				to.setCleric(from.getCleric());
				to.setEngineer(from.getEngineer());
				to.setArtist(from.getArtist());
				to.setFighter(from.getFighter());
				to.setKnight(from.getKnight());
				to.setAssassin(from.getAssassin());
				to.setRanger(from.getRanger());
				to.setWizard(from.getWizard());
				to.setElementalist(from.getElementalist());
				to.setChanter(from.getChanter());
				to.setPriest(from.getPriest());
				to.setGunner(from.getGunner());
				to.setBard(from.getBard());
				to.setRider(from.getRider());
				to.setOptionSlotValue(from.getOptionSlotValue());
				to.setSpecialSlotValue(from.getSpecialSlotValue());
				to.setOptionSlotBonus(from.getOptionSlotBonus());
				to.setBonusAttr1(from.getBonusAttr1());
				to.setBonusAttr3(from.getBonusAttr3());
				to.setBonusAttr8(from.getBonusAttr8());
				to.setBonusAttr2(from.getBonusAttr2());
				to.setBonusAttr9(from.getBonusAttr9());
				to.setBonusApply(from.getBonusApply());
				to.setNoEnchant(from.getNoEnchant());
				to.setMaxEnchantValue(from.getMaxEnchantValue());
				to.setCannotChangeskin(from.getCannotChangeskin());
				to.setUiSoundType(from.getUiSoundType());
				to.setCashItem(from.getCashItem());
				to.setCanSplit(from.getCanSplit());
				to.setItemDropPermitted(from.getItemDropPermitted());
				to.setCanApExtraction(from.getCanApExtraction());
				to.setMesh(from.getMesh());
				to.setMeshChange(from.getMeshChange());
				to.setDefaultColorM(from.getDefaultColorM());
				to.setDefaultColorF(from.getDefaultColorF());
				to.setCanDye(from.getCanDye());
				to.setRacePermitted(from.getRacePermitted());
				to.setBonusAttr4(from.getBonusAttr4());
				to.setBmRestrictCategory(from.getBmRestrictCategory());
				to.setDisposableTradeItem(from.getDisposableTradeItem());
				to.setDisposableTradeItemCount(from.getDisposableTradeItemCount());
				to.setTemporaryExchangeTime(from.getTemporaryExchangeTime());
				to.setVisualSlot(from.getVisualSlot());
				to.setExtractSkinType(from.getExtractSkinType());
				to.setCannotMatterOption(from.getCannotMatterOption());
				to.setCannotExtraction(from.getCannotExtraction());
				to.setAbyssPoint(from.getAbyssPoint());
				to.setBonusAttr12(from.getBonusAttr12());
				to.setAbyssItem(from.getAbyssItem());
				to.setAbyssItemCount(from.getAbyssItemCount());
				to.setDescLong(from.getDescLong());
				to.setExtraCurrencyItem(from.getExtraCurrencyItem());
				to.setExtraCurrencyItemCount(from.getExtraCurrencyItemCount());
				to.setBonusAttr5(from.getBonusAttr5());
				to.setBonusAttr6(from.getBonusAttr6());
				to.setBonusAttr7(from.getBonusAttr7());
				to.setBonusAttr10(from.getBonusAttr10());
				to.setBonusAttr11(from.getBonusAttr11());
				to.setDescCraftman(from.getDescCraftman());
				to.setExpireTime(from.getExpireTime());
				to.setTag(from.getTag());
				to.setGuildLevelPermitted(from.getGuildLevelPermitted());
				to.setUseEmblem(from.getUseEmblem());
				to.setChargeWay(from.getChargeWay());
				to.setChargeLevel(from.getChargeLevel());
				to.setChargePrice1(from.getChargePrice1());
				to.setBonusAttrA1(from.getBonusAttrA1());
				to.setBonusAttrA2(from.getBonusAttrA2());
				to.setBonusAttrA3(from.getBonusAttrA3());
				to.setBonusAttrA4(from.getBonusAttrA4());
				to.setBurnOnAttack(from.getBurnOnAttack());
				to.setBurnOnDefend(from.getBurnOnDefend());
				to.setChargePrice2(from.getChargePrice2());
				to.setBonusAttrB1(from.getBonusAttrB1());
				to.setBonusAttrB2(from.getBonusAttrB2());
				to.setBonusAttrB3(from.getBonusAttrB3());
				to.setBonusAttrB4(from.getBonusAttrB4());
				to.setMagicalDefend(from.getMagicalDefend());
				to.setTradeInItemList(from.getTradeInItemList());
				to.setMagicalSkillBoostResist(from.getMagicalSkillBoostResist());
				to.setRandomOptionSet(from.getRandomOptionSet());
				to.setUsableRankMin(from.getUsableRankMin());
				to.setReidentifyCount(from.getReidentifyCount());
				to.setConfirmToDeleteCashItem(from.getConfirmToDeleteCashItem());
				to.setTradeInAbyssPoint(from.getTradeInAbyssPoint());
				to.setMaxEnchantBonus(from.getMaxEnchantBonus());
				to.setMaxHp(from.getMaxHp());
				to.setPhysicalCriticalReduceRate(from.getPhysicalCriticalReduceRate());
				to.setBlock(from.getBlock());
				to.setDamageReduce(from.getDamageReduce());
				to.setReduceMax(from.getReduceMax());
				to.setUsableRankMax(from.getUsableRankMax());
				to.setCashAvailableMinute(from.getCashAvailableMinute());
				to.setBladeFx(from.getBladeFx());
				to.setItemFx(from.getItemFx());
				to.setGatheringPoint(from.getGatheringPoint());
				to.setWeaponType(from.getWeaponType());
				to.setDmgDecal(from.getDmgDecal());
				to.setTrailTex(from.getTrailTex());
				to.setEquipBone(from.getEquipBone());
				to.setMinDamage(from.getMinDamage());
				to.setMaxDamage(from.getMaxDamage());
				to.setStr(from.getStr());
				to.setAgi(from.getAgi());
				to.setKno(from.getKno());
				to.setHitAccuracy(from.getHitAccuracy());
				to.setCritical(from.getCritical());
				to.setParry(from.getParry());
				to.setMagicalSkillBoost(from.getMagicalSkillBoost());
				to.setMagicalHitAccuracy(from.getMagicalHitAccuracy());
				to.setAttackType(from.getAttackType());
				to.setAttackDelay(from.getAttackDelay());
				to.setHitCount(from.getHitCount());
				to.setAttackGap(from.getAttackGap());
				to.setAttackRange(from.getAttackRange());
				to.setCanProcEnchant(from.getCanProcEnchant());
				to.setCanCompositeWeapon(from.getCanCompositeWeapon());
				to.setCanPolish(from.getCanPolish());
				to.setPolishBurnOnAttack(from.getPolishBurnOnAttack());
				to.setPolishBurnOnDefend(from.getPolishBurnOnDefend());
				to.setCombatEquipBone(from.getCombatEquipBone());
				to.setBasicLength(from.getBasicLength());
				to.setCombatItemFx(from.getCombatItemFx());
				to.setWarriorMax(from.getWarriorMax());
				to.setScoutMax(from.getScoutMax());
				to.setMageMax(from.getMageMax());
				to.setClericMax(from.getClericMax());
				to.setEngineerMax(from.getEngineerMax());
				to.setArtistMax(from.getArtistMax());
				to.setFighterMax(from.getFighterMax());
				to.setKnightMax(from.getKnightMax());
				to.setAssassinMax(from.getAssassinMax());
				to.setRangerMax(from.getRangerMax());
				to.setWizardMax(from.getWizardMax());
				to.setElementalistMax(from.getElementalistMax());
				to.setChanterMax(from.getChanterMax());
				to.setPriestMax(from.getPriestMax());
				to.setGunnerMax(from.getGunnerMax());
				to.setBardMax(from.getBardMax());
				to.setRiderMax(from.getRiderMax());
				to.setTradeInPrice(from.getTradeInPrice());
				to.setAmmoBone(from.getAmmoBone());
				to.setAmmoFx(from.getAmmoFx());
				to.setAmmoSpeed(from.getAmmoSpeed());
				to.setData(from.getData());
				to.setAttackFx(from.getAttackFx());
				to.setRobotName(from.getRobotName());
				to.setExtraInventory(from.getExtraInventory());
				to.setQuest(from.getQuest());
				to.setQuestLabel(from.getQuestLabel());
				to.setRequireShard(from.getRequireShard());
				to.setGainSkill1(from.getGainSkill1());
				to.setGainLevel1(from.getGainLevel1());
				to.setRequireSkill1(from.getRequireSkill1());
				to.setRequireSkill2(from.getRequireSkill2());
				to.setStigmaType(from.getStigmaType());
				to.setGainSkill2(from.getGainSkill2());
				to.setGainLevel2(from.getGainLevel2());
				to.setToolType(from.getToolType());
				to.setMotionName(from.getMotionName());
				to.setCombineskill(from.getCombineskill());
				to.setCategory(from.getCategory());
				to.setBoostStrDesc(from.getBoostStrDesc());
				to.setBoostMaterial(from.getBoostMaterial());
				to.setBoostMaterialNum(from.getBoostMaterialNum());
				to.setDisassemblyItem(from.getDisassemblyItem());
				to.setActivationMode(from.getActivationMode());
				to.setActivationCount(from.getActivationCount());
				to.setBreakdown(from.getBreakdown());
				to.setActivateTarget(from.getActivateTarget());
				to.setUseDelayTypeId(from.getUseDelayTypeId());
				to.setUseDelay(from.getUseDelay());
				to.setCastingDelay(from.getCastingDelay());
				to.setTargetItemCategory(from.getTargetItemCategory());
				to.setApExtractionRate(from.getApExtractionRate());
				to.setSubEnchantMaterialMany(from.getSubEnchantMaterialMany());
				to.setPackagePermitted(from.getPackagePermitted());
				to.setPolishSetName(from.getPolishSetName());
				to.setSubEnchantMaterialEffectType(from.getSubEnchantMaterialEffectType());
				to.setSubEnchantMaterialEffect(from.getSubEnchantMaterialEffect());
				to.setTargetItemLevelMin(from.getTargetItemLevelMin());
				to.setTargetItemLevelMax(from.getTargetItemLevelMax());
				to.setStatEnchantType1(from.getStatEnchantType1());
				to.setStatEnchantValue1(from.getStatEnchantValue1());
				to.setStatEnchantType2(from.getStatEnchantType2());
				to.setStatEnchantValue2(from.getStatEnchantValue2());
				to.setStatEnchantType3(from.getStatEnchantType3());
				to.setStatEnchantValue3(from.getStatEnchantValue3());
				to.setStatEnchantType4(from.getStatEnchantType4());
				to.setStatEnchantValue4(from.getStatEnchantValue4());
				to.setStatEnchantType5(from.getStatEnchantType5());
				to.setStatEnchantValue5(from.getStatEnchantValue5());
				to.setProcEnchantSkill(from.getProcEnchantSkill());
				to.setProcEnchantSkillLevel(from.getProcEnchantSkillLevel());
				to.setProcEnchantEffectOccurProb(from.getProcEnchantEffectOccurProb());
				to.setProcFx(from.getProcFx());
				to.setDescProc(from.getDescProc());
				to.setProcEnchantEffectOccurLeftProb(from.getProcEnchantEffectOccurLeftProb());
				to.setChargeCapacity(from.getChargeCapacity());
				to.setUnitSellCount(from.getUnitSellCount());
				to.setWeaponBoostValue(from.getWeaponBoostValue());
				to.setDyeingColor(from.getDyeingColor());
				to.setSkillToLearn(from.getSkillToLearn());
				to.setUseFx(from.getUseFx());
				to.setUseFxBone(from.getUseFxBone());
				to.setSummonHousingObject(from.getSummonHousingObject());
				to.setDocBg(from.getDocBg());
				to.setCustomPartName(from.getCustomPartName());
				to.setAreaToUse(from.getAreaToUse());
				to.setActivationSkill(from.getActivationSkill());
				to.setActivationLevel(from.getActivationLevel());
				to.setActivateTargetRace(from.getActivateTargetRace());
				to.setEquipType(from.getEquipType());
				to.setDifficulty(from.getDifficulty());
				to.setScale(from.getScale());
				to.setBoosterWing(from.getBoosterWing());
				to.setFuncPetName(from.getFuncPetName());
				to.setFuncPetDurMinute(from.getFuncPetDurMinute());
				to.setCraftRecipeInfo(from.getCraftRecipeInfo());
				to.setDopingPetUseable(from.getDopingPetUseable());
				to.setOwnershipWorld(from.getOwnershipWorld());
				to.setDefaultProhibit(from.getDefaultProhibit());
				to.setRideUsable(from.getRideUsable());
				to.setReturnAlias(from.getReturnAlias());
				to.setReturnWorldid(from.getReturnWorldid());
				to.setDropEachMember(from.getDropEachMember());
				to.setCashSocial(from.getCashSocial());
				to.setCashTitle(from.getCashTitle());
				to.setInvenWarehouseMaxExtendlevel(from.getInvenWarehouseMaxExtendlevel());
				to.setHousingChangeSize(from.getHousingChangeSize());
				to.setCosmeticName(from.getCosmeticName());
				to.setCouponItem(from.getCouponItem());
				to.setCouponItemCount(from.getCouponItemCount());
				to.setToyPetName(from.getToyPetName());
				to.setUseNotice(from.getUseNotice());
				to.setDisassemblyType(from.getDisassemblyType());
				to.setAssemblyItem(from.getAssemblyItem());
				to.setCustomIdleAnim(from.getCustomIdleAnim());
				to.setCustomRunAnim(from.getCustomRunAnim());
				to.setCustomJumpAnim(from.getCustomJumpAnim());
				to.setCustomRestAnim(from.getCustomRestAnim());
				to.setAnimExpireTime(from.getAnimExpireTime());
				to.setCustomShopAnim(from.getCustomShopAnim());
				to.setResetInstanceCooltSyncId(from.getResetInstanceCooltSyncId());
				to.setF2PPackName(from.getF2PPackName());
				to.setF2PPackAvailable(from.getF2PPackAvailable());
				to.setTrialUserCanVendorBuy(from.getTrialUserCanVendorBuy());
				to.setCharBmName(from.getCharBmName());
				to.setCharBmAvailableDuration(from.getCharBmAvailableDuration());
				to.setBonusAddexpItem(from.getBonusAddexpItem());
				to.setMegaphoneRgb(from.getMegaphoneRgb());
				to.setExpExtractionCost(from.getExpExtractionCost());
				to.setExpExtractionReward(from.getExpExtractionReward());
				to.setRideDataName(from.getRideDataName());
				// END
				templateList.add(to);
			}
		}
		else
				System.out.println("[CLIENT - ITEMS][ERROR] Merged list is null ... Check if Configs allow to read/write custom data !");
	}

	@Override
	public void marshall() {
		addOrder(AionReadingConfig.ITEMS_BINDINGS, AionWritingConfig.CLIENT_ITEMS, finalTemplates);
		FileMarhshaller.marshallFile(orders);
		System.out.println("[CLIENT - ITEMS] Items count: " + templateList.size());
	}
}