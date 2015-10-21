package symbiote.entity;

public abstract class LivingEntity extends AbstractEntity {
    public double maxHealth = 100;
    public double health = 100;
    public String name = "";
    public boolean symbioteControlled = false;
    
    public LivingEntity(int id, double x, double y) {
        super(id, x, y);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (health < 0) {
            health = 0;
            // TODO: destroy when health <= 0?
        }
    }
}
