package mrhart1ey.gomoku.player.gui.menu;

import java.util.Stack;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import mrhart1ey.gomoku.GameConfiguration;
import mrhart1ey.gomoku.SessionConfiguration;
import mrhart1ey.gomoku.SingleTimePasser;
import mrhart1ey.gomoku.player.gui.GameDisplay;

public class GameMenu {

    private final SingleTimePasser<SessionConfiguration> sessionConfigPasser;
    private final SingleTimePasser<GameConfiguration> gameConfigPasser;
    private final SingleTimePasser<String> hostPasser;

    private final JFrame window;

    private final JPanel mainMenu;

    private final MenuScreenTransition menuScreenTransition;

    public GameMenu(GameDisplay display) {
        sessionConfigPasser = new SingleTimePasser<>();
        gameConfigPasser = new SingleTimePasser<>();
        hostPasser = new SingleTimePasser<>();

        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setResizable(false);

        Stack<JComponent> menuScreenHistory = new Stack<>();

        menuScreenTransition = (currentScreen, nextMenuScreen) -> {
            SwingUtilities.invokeLater(() -> {
                if (currentScreen != null) {
                    menuScreenHistory.push(currentScreen);
                }
                window.getContentPane().removeAll();

                window.add(nextMenuScreen);

                window.revalidate();

                window.repaint();

                window.pack();
            });
        };

        SingleTimePasser<Thread> choosenSessionConfigurationThread
                = new SingleTimePasser<>();

        JPanel singlePlayerGameConfigMenu
                = new GameConfigurationMenuScreen(
                        menuScreenTransition, new JPanel(), menuScreenHistory,
                        choosenSessionConfigurationThread, gameConfigPasser);

        Runnable yourselfSessionConfigurationHandler
                = new YourselfSessionConfigurationHandler(display,
                        sessionConfigPasser, gameConfigPasser);

        Runnable computerSessionConfigurationHandler
                = new ComputerSessionConfigurationHandler(display,
                        sessionConfigPasser, gameConfigPasser);

        JPanel singlePlayerMenu
                = new SinglePlayerMenuScreen(menuScreenTransition,
                        singlePlayerGameConfigMenu,
                        singlePlayerGameConfigMenu,
                        menuScreenHistory,
                        choosenSessionConfigurationThread,
                        yourselfSessionConfigurationHandler,
                        computerSessionConfigurationHandler);

        JPanel multiplayerHostGameConfigMenu
                = new GameConfigurationMenuScreen(
                        menuScreenTransition,
                        new HostWaitingMenuScreen(menuScreenTransition,
                                menuScreenHistory, choosenSessionConfigurationThread),
                        menuScreenHistory,
                        choosenSessionConfigurationThread,
                        gameConfigPasser);

        JPanel joinMenu = new JoinMenuScreen(menuScreenTransition,
                new JoiningHostMenuScreen(menuScreenTransition,
                        menuScreenHistory, choosenSessionConfigurationThread),
                menuScreenHistory,
                choosenSessionConfigurationThread,
                hostPasser);

        Runnable joinSessionConfigurationHandler
                = new JoinSessionConfigurationHandler(display,
                        sessionConfigPasser, hostPasser);

        Runnable hostSessionConfigurationHandler
                = new HostSessionConfigurationHandler(display,
                        sessionConfigPasser, gameConfigPasser);

        JPanel multiplayerPlayerMenu
                = new MultiplayerMenuScreen(menuScreenTransition,
                        multiplayerHostGameConfigMenu,
                        joinMenu, menuScreenHistory,
                        choosenSessionConfigurationThread,
                        hostSessionConfigurationHandler,
                        joinSessionConfigurationHandler);

        mainMenu = new MainMenuScreen(
                menuScreenTransition, singlePlayerMenu,
                multiplayerPlayerMenu);

        window.add(mainMenu);
    }

    public void show() {
        menuScreenTransition.transition(null, mainMenu);
        window.setVisible(true);
    }

    public SessionConfiguration waitForUserToChooseTheSessionConfig()
            throws InterruptedException {
        return sessionConfigPasser.take();
    }

    public void stopShowing() {
        window.setVisible(false);
        window.dispose();
    }
}
