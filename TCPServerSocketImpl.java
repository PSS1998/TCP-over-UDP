import java.net.DatagramPacket;

public class TCPServerSocketImpl extends TCPServerSocket {
    private EnhancedDatagramSocket UDPSocket;

    public TCPServerSocketImpl(int port) throws Exception {
        super(port);
        this.UDPSocket = new EnhancedDatagramSocket(port);
    }

    @Override
    public TCPSocket accept() throws Exception {
        DatagramPacket UDPPacket = null;
        TCPPacket req = null;
        while (req == null || !(req.getSYN() && !req.getACK())) {
            if (req != null)
                System.err.println("Invalid TCP Package");
            byte[] data = new byte[this.UDPSocket.getPayloadLimitInBytes()];
            UDPPacket = new DatagramPacket(data, data.length);
            this.UDPSocket.receive(UDPPacket);
            req = new TCPPacket(data);
        }
        return new TCPSocketImpl(UDPPacket.getAddress().getHostAddress(), UDPPacket.getPort(), req);
    }

    @Override
    public void close() throws Exception {
        this.UDPSocket.close();
    }
}
