package symbiote;

public class PacketHandler {
    
    public String packetType;
    
    public PacketHandler(String packetType) {
        this.packetType = packetType;       
    }
 
    public void onPacket(String packet, String[] packetParts) {
        System.out.println(packetType + "'s listener doesn't have any code for when packets are received!");
    }
}

