package symbiote.entity;

import symbiote.client.Client;
import symbiote.resources.AnimationFactory;
import symbiote.world.Wall;

public class EntityBullet extends Entity {

    public String owner = null;

    public EntityBullet(double x, double y, double angle, String owner) {
        super(x, y);
        this.angle = angle;
        this.owner = owner;
        velocityDecrease = false;
        speed = 8;
        xVel = speed * Math.sin(Math.toRadians(angle));
        yVel = speed * -Math.cos(Math.toRadians(angle));
        animation = AnimationFactory.start().addFrame("bullet.png").finish();
    }

    @Override
    public void tick() {
        super.tick();
        if (x > 800 || x < 0 || y > 600 || y < 0) {
            destroy();
        }
    }

    @Override
    public void collide(Wall w) {
        destroy();
    }

    @Override
    public void collide(Entity e) {
        boolean living = false;
        boolean hit = true;
        if (e instanceof LivingEntity) living = true;
        if (e instanceof EntityPlayer) {
            if (((EntityPlayer)e).name.equals(owner)) {
                hit = false;
            }
        }
        if (hit) {
            if (living) {
                ((LivingEntity)e).health -= 10;
            }
            destroy();
        }
    }
}
