package observatory.observations.common.registry;

import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import observatory.observations.common.component.PrivilegedComponent;
import observatory.observations.common.util.TraitUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum Trait {
    // * NICKEL (he left so these don't need to be finished lol)
    //Done
    HALF_GILLS("When touching any water, gain conduit power, but when not touching water gain weakness."),
    TOP_OF_THE_FOOD_CHAIN("All hostile underwater mobs are passive, but dolphins are always aggressive."),
    //Done
    BLOOD_SCENT("Entities which are touching any form of water glow to you."),

    // * VANNIE
    WE_RIDE_AT_SUNSET("Horses that you ride are faster than normal."),
    FASTEST_HANDS_IN_THE_SYSTEM("Charge/Load all ranged weapons faster."),
    THE_ROCKETS_RED_GLARE("Indirect damage like explosion that you cause do more damage."),
    // ALSO VANNIE
    // * Done
    NO_LONGER_FLESH("You are made out of stone, causing you to sink and water, but if timed correctly you can skip across water by jumping."),
    // * Done
    GEODE_SOUL("While standing on budding amethyst, a bud will grow within yourself that nullifies damage, breaking when used up. You have increased damage resistance."),
    // * Done
    OBVIOUS_CRACKS("Pickaxes deal double damage to you and slow you down upon hit."),

    // NOPEABLE (these might be changed to something else? screw you nopeable!!)
    // * Done
    DIVINE_INTERVENTION("You have a 15% buff in many minor stats, this includes but is not limited to, attack damage, speed, attack speed, hunger and regeneration.", TraitUtil.multiplyBaseModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.15), TraitUtil.multiplyBaseModifier(EntityAttributes.GENERIC_ATTACK_SPEED, 0.15), TraitUtil.multiplyBaseModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15)),
    ADRENALINE("All of your previous buffs are instead increased to a 25% increase when your on 3 hearts or less."),
    DISARMED("You do not have your left arm. You cannot use your offhand or use items like reloading a Crossbow or holding an Amarite Longsword."),
    // ALSO NOPEABLE
    SQUISH("You can naturally move into smaller spaces easier. For example, you can go into crawl when sprinting into a 1 block tall opening"),
    BANDIT("You're naturally faster and can wallrun.", TraitUtil.multiplyBaseModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15)),
    PICKPOCKET("You have constant looting from Mobs."),

    // *  HARPER (bruh drug abuse why do I have to make this)
    // * Done
    SHAPES_AND_COLOURS("After 5 minutes, there is a 50% chance for me to get a screen effect that simulates the effects of drugs for 1 minute."),
    // *  Done
    NUTRITIONAL("Her nutritional gas and inability to remove the mask causes her to no longer be able to eat or drink potions, for slower hunger regeneration."),
    // * Done
    GASSED_UP("The permanent high effecting her body has caused her to gain more energy and her neurons to fire faster.", TraitUtil.multiplyBaseModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)),

    // HAILEY
    // * Done
    WEEEE("Allows you to throw entities 3-4 blocks in the air when hitting them with your hands"),
    // * Done
    HEAVY_HITTER("Decreased attack speed, increased attack damage", TraitUtil.multiplyBaseModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.5)),
    // * Done
    WEIGHED_DOWN("Increased knockback resistance, but constant slowness", TraitUtil.additionModifier(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.3), TraitUtil.multiplyBaseModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, -0.075)),

    // PLURIXITY
    // * Done
    CRESCENT_THIEF("Copies the nearest player's abilities."),

    // KAJ0JAJ0
    // * Done
    SOULLESS_CREATURE("You're not really alive, so most monsters don't attack you, but since you're not alive you don't have natural regeneration."),
    // * Done
    STRONG_HANDS_EVEN_STRONGER_MORALS("Increased attack damage in sunlight, but you can't get any player lower than half a heart."),
    // * Done (although it could be that kajo wants health to regen in sunlight and not hunger lmao)
    PHOTOSYNTHESIS("Being a plant makes it so you can't eat food, you have to rely on a source of light."),

    // PIPOBUTTER
    // * Done
    MAGMA_COVERED("Because of your magma skin gain fire immunity and you can swim in lava just like water, but you cannot swim in water as you will sink because your skin rapidly cools."),
    // * Done
    FAMILIARITY("You're a nether born creature and others see you as neutral, if you attack them they will attack back."),
    // * Done
    COMFORTING_WARMTH("While being on fire you gain regen one and if inside of lava you have regen 2."),

    // WAZZO
    SLIPPERY("All blocks behave as ice, making me frictionless."),
    TALL("Step height is increased to a full block."),
    ACCELERATION_MATRIX("because I am frictionless the longer I run the faster I go, and at certain thresholds I gain positive effects. The first is increased attack speed (3X), then even faster I’ll just do damage to people by running into people, and at “maximum” speed I can run on water (and lava if wearing netherite boots), all with the caveat that the faster I go, if I were to crash, I take exponentially increased damage."),
    // WAZZO BUT NEW???
    // * Done
    BUG_LIKE_APPEARANCE("You look and see like a Bug. Other Arthropods will not attack you, and will be neutral if they were hostile towards you. You also have Compound Eyes and are immune to visual effects."),
    // * Done
    SILK_SACK("In your inventory, you have a button on the right-side of your slots which you can click to open 6 extra slots. These slots are kept after death and can be hidden by pressing the button again."),
    // * Done
    AGILE("You have extra mobility, you can stick to walls, climb up walls and wall-jump. In addition, you can jump higher.", TraitUtil.multiplyBaseModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25), TraitUtil.multiplyBaseModifier(AdditionalEntityAttributes.JUMP_HEIGHT, 1.2)),

    // EVEREST
    // * Done
    STELLAR_FEEDER("During the night time, i can look up at the sky to get food ( NOT SATURATION ), max of three bars,  and constellation particles are present"),
    // * Done
    GRAVITY_CORE("Slow falling 3 when more than 3 blocks away from the ground, but no fall damage"),
    // * Done
    ENTROPY_PULSE("Consumable items have a 10% chance to not be used but stay in inventory, but also a 15% chance to get consumed and not used"),

    // RAFSA
    // * Done
    STRONGER_THAN_STONE("After being in the caves so long, you have had stone, moss and anything that doesn't or has yet to exist make its home on your body, making you tankier (+2 hearts) but being extremely slow (slowness 2).", TraitUtil.additionModifier(EntityAttributes.GENERIC_MAX_HEALTH, 2), TraitUtil.multiplyBaseModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, -0.3)),
    // * Done
    WILL_TO_LIVE_ON("While the infection still tries to weaken you, (less mining speed on everything) your pure determination to live on allows you to have higher stats based on health (+4% to general stats, speed both movement and attack, damage, etc. (just copy divine intervention from nope i aint being original fuck allat) per half-heart lost)."),
    // * Done
    INFECTION("While you are tall and strong, you aren't immune to disease and corruption, being infected by an indescribable, unkillable infection, only weakened by heat, and while it hides under your hard exterior, it takes your energy and gives you its weaknesses, making you weaker to fire (fire/lava) but stronger against frost (freeze/frostbite)."),

    // WINTER
    // * Done
    PRIVILEGED_PLATES("Gold armor has properties now."),
    // * Done
    SMOKED_LUNGS("You drown quicker and at high altitudes."),
    // * Done
    AURIC_ARTERIES("Your defense is increased.", TraitUtil.additionModifier(EntityAttributes.GENERIC_ARMOR, 6)),

    // POM
    // * Done
    NO_ENZYMES("You cannot get hungry or eat, but you regenerate health faster."),
    // * Done
    LINE_OF_SIGHT("Players that are looked at will be highlighted for you."),
    // * Done
    ACROBATICS("You have an increased movement speed and jump height.", TraitUtil.multiplyBaseModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)),

    // SHINY
    // * Done
    WEIGHTLESS("You benefit from complete weightlessness, rendering you immune to the forces of gravity, amplifying your sprinting speed in the air and resulting in twice as much knockback received."),
    // * Done (inside weightless)
    INFINITE_FREEDOM("Not being limited to the ground, you can move freely in the air. Forwards or backwards movement on your behalf is no longer purely horizontal."),
    // * Done; add additional items to the observations:projectile_weapons tag
    LIKE_VOID("Having virtually no mass, you possess little strength. Projectile-based weapons refuse your command. Receiving damage past a certain threshold in a single attack renders you incapacitated, temporarily preventing you from moving."),

    // * Done
    FIVE_BIG_BOOMS("Upon falling below 50% health, a five-second countdown initiates; failure to defuse results in a catastrophic self-detonation."),
    // * Done (inside five big booms)
    DEFUSAL("If the detonation timer is currently active jumping into water will cancel it."),

    FLOATY("You have permanent Slow Falling and lower Gravity when jumping, in addition, if you stand completely still for 4 seconds you will begin to float upwards and stops when you start moving.\n"),
    DENSE("You cannot stay in water for long, being thrust to the surface when underwater.\n"),
    POPPING("You only have 8 hearts.", TraitUtil.multiplyBaseModifier(EntityAttributes.GENERIC_MAX_HEALTH, -0.2));

    private final String id;
    private final DefaultedList<Pair<EntityAttribute, EntityAttributeModifier>> attributes;
    private boolean wasCopied;

    Trait(String description, Pair<EntityAttribute, EntityAttributeModifier>... entityAttributeModifiers) {
        this.id = this.toString().toLowerCase(Locale.ROOT);
        this.attributes = DefaultedList.copyOf(TraitUtil.additionModifier(EntityAttributes.GENERIC_LUCK, 0), entityAttributeModifiers);
        this.wasCopied = false;
    }

    public String getId() {
        return this.id;
    }

    public DefaultedList<Pair<EntityAttribute, EntityAttributeModifier>> getModifiedAttributes() {
        return this.attributes;
    }

    public EntityAttributeModifier getModifierFor(EntityAttribute attribute) {
        for (Pair<EntityAttribute, EntityAttributeModifier> pair : this.attributes) {
            if (attribute == pair.getLeft()) {
                return pair.getRight();
            }
        }
        return null;
    }

    public static @Nullable Trait fromString(String id) {
        for(Trait trait : values()) {
            if (trait.id.equalsIgnoreCase(id)) {
                return trait;
            }
        }

        return null;
    }

    public String formatTraitName(String traitName) {
        String[] words = traitName.split("_");
        StringBuilder formattedName = new StringBuilder();

        for (String word : words) {
            formattedName.append(word.substring(0, 1).toUpperCase());
            formattedName.append(word.substring(1).toLowerCase());
            formattedName.append(" ");
        }

        return formattedName.toString().trim();
    }

    public void setCopied(boolean wasCopied) {
        this.wasCopied = wasCopied;
    }

    public boolean isCopied() {
        return this.wasCopied;
    }

}
