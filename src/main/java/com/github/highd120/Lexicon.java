package com.github.highd120;

import com.github.highd120.item.ItemList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;

public class Lexicon {
    public static LexiconEntry runeEntry;
    public static LexiconEntry shotSwordEntry;
    public static LexiconEntry bindSwordEntry;

    public static void init() {
    	runeEntry = new LexiconEntry("lexicon.rune.name", BotaniaAPI.categoryMana);
    	runeEntry.setLexiconPages(BotaniaAPI.internalHandler.textPage("lexicon.rune.page0"),
        		BotaniaAPI.internalHandler.runeRecipePage("lexicon.rune.page1", RecipeList.runeRecipe)).
                setIcon(new ItemStack(ItemList.runeItem));
        BotaniaAPI.addEntry(runeEntry, BotaniaAPI.categoryMana);

        shotSwordEntry = new LexiconEntry("lexicon.shot_sword.name", BotaniaAPI.categoryMana);
        shotSwordEntry.setLexiconPages(BotaniaAPI.internalHandler.textPage("lexicon.shot_sword.page0"),
        		BotaniaAPI.internalHandler.craftingRecipePage("lexicon.shot_sword.page1", RecipeList.shotSwordRecipe)).
                setIcon(new ItemStack(ItemList.shotSwordItem));
        BotaniaAPI.addEntry(shotSwordEntry, BotaniaAPI.categoryMana);

    	bindSwordEntry = new LexiconEntry("lexicon.bindsword.name", BotaniaAPI.categoryMana) {
    		public String getTagline() {
    			return "lexicon.bindsword.tagline";
    		}
    	};
    	bindSwordEntry.setLexiconPages(BotaniaAPI.internalHandler.textPage("lexicon.bindsword.page0"),
        		BotaniaAPI.internalHandler.petalRecipePage("lexicon.bindsword.page1", RecipeList.bindSwordFlowerRecipe)).
                setIcon(RecipeList.getFlower("bindsword"));
        BotaniaAPI.addEntry(bindSwordEntry, BotaniaAPI.categoryFunctionalFlowers);
    }
}
