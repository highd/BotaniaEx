package com.github.highd120;

import com.github.highd120.block.BlockStand;
import com.github.highd120.block.SubTileBindSword;
import com.github.highd120.block.SubTileCreateManaFluid;
import com.github.highd120.block.SubTileFallingBlock;
import com.github.highd120.block.injection.BlockInjection;
import com.github.highd120.item.RuneItem;
import com.github.highd120.item.ShotSwordItem;
import com.github.highd120.util.item.ItemManager;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipeRuneAltar;

/**
 * レキシコンのリスト。
 * @author hdgam
 */
public class Lexicon {
    public static LexiconEntry runeEntry;
    public static LexiconEntry injectionEntry;
    public static LexiconEntry bindSwordEntry;
    public static LexiconEntry createManaFlowerEntry;
    public static LexiconEntry fallingBlockEntry;
    public static LexiconEntry shotSwordEntry;
    public static LexiconEntry standEntry;

    private static LexiconEntry createEntry(String name, ItemStack icon, int pageNumber,
            LexiconCategory category, LexiconPage lastPage) {
        LexiconEntry entry = new LexiconEntry("lexicon." + name + ".name", category) {
            public String getTagline() {
                return "lexicon." + name + ".tagline";
            }
        };
        LexiconPage[] pages = new LexiconPage[pageNumber + 1];
        for (int i = 0; i < pageNumber; i++) {
            pages[i] = BotaniaAPI.internalHandler.textPage("lexicon." + name + ".page" + i);
        }
        pages[pageNumber] = lastPage;
        entry.setLexiconPages(pages);
        entry.setIcon(icon);
        return entry;

    }

    private static LexiconEntry createEntryCraft(String name, ItemStack icon, int pageNumber,
            ShapedOreRecipe recipe, LexiconCategory category) {
        String last = "lexicon." + name + ".page" + pageNumber;
        LexiconPage lastPage = BotaniaAPI.internalHandler.craftingRecipePage(last, recipe);
        LexiconEntry entry = createEntry(name, icon, pageNumber, category, lastPage);
        BotaniaAPI.addEntry(entry, category);
        return entry;
    }

    private static LexiconEntry createEntryRune(String name, ItemStack icon, int pageNumber,
            RecipeRuneAltar recipe, LexiconCategory category) {
        String last = "lexicon." + name + ".page" + pageNumber;
        LexiconPage lastPage = BotaniaAPI.internalHandler.runeRecipePage(last, recipe);
        LexiconEntry entry = createEntry(name, icon, pageNumber, category, lastPage);
        BotaniaAPI.addEntry(entry, category);
        return entry;
    }

    private static LexiconEntry createEntryPetal(String name, ItemStack icon, int pageNumber,
            RecipePetals recipe, LexiconCategory category) {
        String last = "lexicon." + name + ".page" + pageNumber;
        LexiconPage lastPage = BotaniaAPI.internalHandler.petalRecipePage(last, recipe);
        LexiconEntry entry = createEntry(name, icon, pageNumber, category, lastPage);
        BotaniaAPI.addEntry(entry, category);
        return entry;
    }

    /**
     * レキシコンへの登録。
     */
    public static void init() {
        runeEntry = createEntryRune("rune", ItemManager.getItemStack(RuneItem.class), 1,
                RecipeList.runeRecipe, BotaniaAPI.categoryMana);

        injectionEntry = createEntryRune("injection",
                ItemManager.getItemStack(BlockInjection.class), 2, RecipeList.injectionCoreRecipe,
                BotaniaAPI.categoryMana);

        bindSwordEntry = createEntryPetal("bindsword", RecipeList.getFlower(SubTileBindSword.NAME),
                2, RecipeList.bindSwordFlowerRecipe, BotaniaAPI.categoryFunctionalFlowers);

        createManaFlowerEntry = createEntryPetal("createmanafluid",
                RecipeList.getFlower(SubTileCreateManaFluid.NAME), 2,
                RecipeList.createManaFlowerRecipe, BotaniaAPI.categoryFunctionalFlowers);

        fallingBlockEntry = createEntryPetal("fallingblock",
                RecipeList.getFlower(SubTileFallingBlock.NAME),
                2, RecipeList.fallingBlockRecipe, BotaniaAPI.categoryGenerationFlowers);

        shotSwordEntry = createEntryCraft("shot_sword",
                ItemManager.getItemStack(ShotSwordItem.class), 1, RecipeList.shotSwordRecipe,
                BotaniaAPI.categoryMana);

        standEntry = createEntryCraft("stand", ItemManager.getItemStack(BlockStand.class), 1,
                RecipeList.standCoreRecipe, BotaniaAPI.categoryMana);
    }
}
