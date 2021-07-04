package me.kp.moon.moonpvp.kit;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public enum KitType {

    AJNIN("Ajnin", 9500, "Teleporte seu oponente até você.", Material.NETHER_STAR, null, true),
    ANCHOR("Anchor", 10000, "Não tome nem aplique repulsão.", Material.ANVIL, null, true),
    ANTISTOMPER("AntiStomper", 5000, "Seja imune a Stompers.", Material.DIAMOND_HELMET, null, true),
    ARCHER("Archer", 5000, "Utilize seu arco e flecha para lutar contra oponentes.", Material.BOW, null, true),
    BOXER("Boxer", 10000, "Seja um pugilista e mostre sua resistência.", Material.IRON_CHESTPLATE, null, true),
    CAMEL("Camel", 8000, "Receba vantagens em seu habitat natural.", Material.SAND, null, true),
    CRITICAL("Critical", 9500, "Aplique danos críticos aos seus inimigos.", Material.REDSTONE_BLOCK, null, true),
    FISHERMAN("Fisherman", 8000, "Pesque seus inimigos.", Material.FISHING_ROD, Material.FISHING_ROD, true),
    FLASH("Flash", 5000, "Percorra grandes distâncias em altas velocidades.", Material.REDSTONE_TORCH_ON, Material.REDSTONE_TORCH_ON, false),
    GLADIATOR("Gladiator", 12000, "Puxe seus oponentes para duelos individuais em uma arena.", Material.IRON_FENCE, Material.IRON_FENCE, true),
    GRANDPA("Grandpa", 5000, "Arremesse seus inimigos para longe.", Material.STICK, Material.STICK, true),
    KANGAROO("Kangaroo", 9500, "Pule igual um canguru.", Material.FIREWORK, Material.FIREWORK, false),
    LEECH("Leech", 10000, "Tenha chance de roubar vida de seus oponentes", Material.FERMENTED_SPIDER_EYE, null, true),
    MAGMA("Magma", 8000, "Tenha chance de colocar fogo em seus oponentes.", Material.FLINT_AND_STEEL, null, true),
    MONK("Monk", 8000, "Embaralhe o inventário do seu oponente.", Material.BLAZE_ROD, Material.BLAZE_ROD, true),
    NINJA("Ninja", 9500, "Teleporte-se para seu oponente.", Material.ENDER_PEARL, null, true),
    POSEIDON("Poseidon", 5000, "Torne-se o deus do mar e receba seus poderes.", Material.WATER_BUCKET, null, false),
    PVP("PvP", 0, "Kit sem habilidades, apenas com uma espada afiada.", Material.STONE_SWORD, null, true),
    REAPER("Reaper", 5000, "Torne-se reencarnação da morte e ceife seus oponentes.", Material.WOOD_HOE, Material.WOOD_HOE, false),
    SCOUT("Scout", 8000, "Aumente sua velocidade com suas botas mágicas.", Material.GOLD_BOOTS, Material.GOLD_BOOTS, false),
    SNAIL("Snail", 9500, "Tenha chances de deixar seus oponentes lentos.", Material.SOUL_SAND, null, true),
    SPECIALIST("Specialist", 9500, "Encante sua espada derrotando seus inimigos.", Material.BOOK, null, true),
    STOMPER("Stomper", 10000, "Pule de grandes alturas e esmague seus oponentes.", Material.IRON_BOOTS, null, true),
    SWITCHER("Switcher", 8000, "Troque de lugar com quem você atingir.", Material.SNOW_BALL, Material.SNOW_BALL, false),
    THOR("Thor", 10000, "Invoque trovões com seu machado.", Material.WOOD_AXE, Material.WOOD_AXE, true),
    TURTLE("Turtle", 8000, "Usufrua de seu casco inquebrável e receba pouco dano.", Material.DIAMOND_CHESTPLATE, null, true),
    URGAL("Urgal", 9500, "Aumente sua força com sua flor mágica.", Material.RED_ROSE, Material.RED_ROSE, false),
    VAMPIRE("Vampire", 9500, "Sugue as energias vitais de um oponente ao matá-lo.", Material.REDSTONE, null, true),
    VIPER("Viper", 9500, "Tenha chances de envenenar seus oponentes.", Material.SPIDER_EYE, null, true),
    ;

    private final String kitname;
    private final int price;
    private final String description;
    private final Material icon;
    private final Material item;
    private final boolean enabled;
    KitType(String kitname, int price, String description, Material icon, Material item, boolean enabled) {
        this.kitname = kitname;
        this.price = price;
        this.description = description;
        this.icon = icon;
        this.item = item;
        this.enabled = enabled;
    }

    public static KitType getKitTypeByName(String kitname) {
        KitType[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final KitType kitType = values[i];
            if (kitType.name().equalsIgnoreCase(kitname)) {
                return kitType;
            }
        }
        return null;
    }

}
