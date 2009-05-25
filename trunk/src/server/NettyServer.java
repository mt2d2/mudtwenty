package server;

import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

import message.ClientMessage;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class NettyServer
{
	public static void main(String[] args) throws Exception
	{
		ChannelFactory factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		ChannelPipeline pipeline = bootstrap.getPipeline();

		// Decoders
		final NettyServerHandler nsh = new NettyServerHandler();
		pipeline.addLast("handler", nsh);

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run()
			{
				nsh.sendMessage(new ClientMessage("Timer interrupt."));
			}
		}, 0, 1000);

		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);

		bootstrap.bind(new InetSocketAddress(8080));

		System.out.println("booting server");
	}
}