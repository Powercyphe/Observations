package observatory.observations.common.registry;

import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import observatory.observations.common.util.TraitUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum Trait {

    // NICKEL
    HALF_GILLS("When Touching Any Water (including rain) Gain Conduit Power But When Not Touching Water Gain Weakness"),
    TOP_OF_THE_FOOD_CHAIN("All Hostile Underwater Mobs Are Passive But Dolphins Are Always Aggressive"),
    BLOOD_SCENT("When A Player Is In Water Or Touching Water (including rain) They Gain The Glowing Effect (Client Side Ofc)"),
    // VANNIE
    WE_RIDE_AT_SUNSET("Horses that you ride are faster than normal"),
    FASTEST_HANDS_IN_THE_SYSTEM("Charge/Load all ranged weapons faster"),
    THE_ROCKETS_RED_GLARE("Indirect damage like explosion that you cause do more damage"),

    // RESISTANCE UNDONE!!!
    NO_LONGER_FLESH("You are made out of stone, causing you to sink and water, but if timed correctly you can skip across water by jumping"),
    GEODE_SOUL("While standing on budding amethyst, a bud will grow within yourself that nullifies damage, breaking when used up. You are also have more damage resistance"),
    OBVIOUS_CRACKS("Pickaxes deal double damage to you and slow you down upon hit"),
    // NOPEABLE
    DIVINE_INTERVENTION("You have a 15% buff in many minor stats, this includes but is not limited to, attack damage, speed, attack speed, hunger and regeneration"),
    ADRENALINE("All of your previous buffs are instead increased to a 25% increase when your on 3 hearts or less"),
    DISARMED("You do not have your left arm. You cannot use your offhand or use items like reloading a Crossbow or holding an Amarite Longsword"),
    // HARPER
    SHAPES_AND_COLOURS("After 5 minutes, there is a 50% chance for me to get a screen effect that simulates the effects of drugs for 1 minute"),
    NUTRITIONAL("Her nutritional gas and inability  to remove the mask causes her to no longer be able to eat or drink potions, for slower hunger regeneration"),
    GASSED_UP("The permanent high effecting her body has caused her to gain more energy and her neurons to fire faster"),
    // HAILEY
    WEEEE("Throws people 3-4 blocks in the air [only when hitting someone with their hands]"),
    HEAVY_HITTER("Decreased attack speed, increased attack damage"),
    WEIGHED_DOWN("Increased knockback resistance, but has constant slowness 1"),
    // PLURIXITY
    CRESCENT_THIEF("Copies/steals the nearest Players abilities"),
    // KAJ0JAJ0
    SOULLESS_CREATURE("You're not really alive so most monsters dont attack you but since youre not alive you dont have natural regeneration"),
    STRONG_HANDS_EVEN_STRONGER_MORALS("You deal more base damage (only in sunlight) but you can't get any player lower than half a heart"),
    PHOTOSYNTHESIS("Being a plant makes it so you can't eat food, you have to rely on a source of light"),
    // PIPOBUTTER
    MAGMA_COVERED("Because of your magma skin gain fire immunity and  you can swim in lava just like water, But you cannot swim in water as you  will sink because your skin rapid cooling."),
    FAMILIARITY("You're a nether born creature and others see you as neutral, if you attack them they will attack back."),
    COMFORTING_WARMTH("While being on fire you gain regen one and if inside of lava you have regen 2"),
    // WAZZO
    SLIPPERY("all blocks behave as ice, making me frictionless"),
    TALL("step height increase to a full block"),
    ACCELERATION_MATRIX("because I am frictionless the longer I run the faster I go, and at certain thresholds I gain positive effects. The first is increased attack speed (3X), then even faster I’ll just do damage to people by running into people, and at “maximum” speed I can run on water (and lava if wearing netherite boots), all with the caveat that the faster I go, if I were to crash, I take exponentially increased"),
    // RAFSA
    STRONGER_THAN_STONE("After being in the caves so long, you have had stone, moss and anything that doesn't or has yet to exist make its home on your body, making you tankier (+2 hearts) but being extremely slow (slowness 2)"),
    WILL_TO_LIVE_ON("While the infection still tries to weaken you, (less mining speed on everything) your pure determination to live on allows you to have higher stats based on health (+4% to general stats, speed both movement and attack, damage, etc. (just copy divine intervention from nope i aint being original fuck allat) per half-heart lost)"),
    INFECTION("While you are tall and strong, you aren't immune to disease and corruption, being infected by an indescribable, unkillable infection, only weakened by heat, and while it hides under your hard exterior, it takes your energy and gives you its weaknesses, making you weaker to fire (fire/lava) but stronger against frost (freeze/frostbite)"),
    // WINTER
    PRIVILEGED_PLATES("Gold armor has properties now"),
    SMOKED_LUNGS("You drown quicker and at high altitudes."),
    AURIC_ARTERIES("Your defense is increased."),
    // POM
    LINE_OF_SIGHT("Players that are looked at will be highlighted for you."),
    NO_ENZYMES("You cannot get hungry or eat, but you regenerate health faster."),
    ACROBATICS("You have an increase movement speed and jump height.", TraitUtil.multiplyTotalModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.005), TraitUtil.multiplyTotalModifier(AdditionalEntityAttributes.JUMP_HEIGHT, 1.2)),
    // SHINY
    WEIGHTLESS("You benefit from complete weightlessness, rendering you immune to the forces of gravity, amplifying your sprinting speed in the air and resulting in twice as much knockback received."),
    INFINITE_FREEDOM("Not being limited to the ground, you can move freely in the air. Forwards or backwards movement on your behalf is no longer purely horizontal."),
    LIKE_VOID("Having virtually no mass, you possess little strength. Projectile-based weapons refuse your command. Receiving damage past a certain threshold in a single attack renders you incapacitated, temporarily preventing you from moving.");

    private final String id;
    private final DefaultedList<Pair<EntityAttribute, EntityAttributeModifier>> attributes;

    Trait(String description, Pair<EntityAttribute, EntityAttributeModifier>... entityAttributeModifiers) {
        this.id = this.toString().toLowerCase(Locale.ROOT);
        this.attributes = DefaultedList.copyOf(TraitUtil.additionModifier(EntityAttributes.GENERIC_LUCK, 0), entityAttributeModifiers);
    }

    public String getId() {
        return this.id;
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
}
