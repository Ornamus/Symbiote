package symbiote.network;

public class SPacketPing extends AbstractPacket {

    String msg;

    public SPacketPing(String msg) {
        this.msg = msg;
    }

    @Override
    public void handle(Communicator comm) {
        System.out.println(this.msg);
    }

}
