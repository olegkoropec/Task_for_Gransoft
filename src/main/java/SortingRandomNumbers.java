import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SortingRandomNumbers extends JFrame {
    private final JTextField numberInput;
    private JPanel numbersPanel;
    private int[] numbers;
    private boolean isAscending = true;
    int count;
    JPanel inputPanel;

    public SortingRandomNumbers() {
        setTitle("Intro screen");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.add(Box.createVerticalStrut(100));

        JLabel inputLabel = new JLabel("How many numbers to display?");
        inputLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        numberInput = new JTextField();
        numberInput.setMaximumSize(new Dimension(90, 30));
        numberInput.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton enterButton = new JButton("Enter");
        enterButton.setBackground(Color.blue);
        enterButton.setForeground(Color.white);
        enterButton.setMaximumSize(new Dimension(90, 30));
        enterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        enterButton.addActionListener(e -> {
            String inputText = numberInput.getText();
            try {
                count = Integer.parseInt(inputText);
                showNumbersScreen(count);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a number.");
            }
        });
        inputPanel.add(inputLabel);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(numberInput);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(enterButton);
        add(inputPanel);
    }

    private void showNumbersScreen(int count) {
        JFrame numbersFrame = new JFrame("Numbers Screen");
        numbersFrame.setSize(count / 10 * 70 + 200, 400);
        numbersFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        numbersFrame.setLocationRelativeTo(null);

        numbersPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(numbersPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JButton sortButton = new JButton("Sort");
        sortButton.setForeground(Color.WHITE);
        sortButton.setBackground(Color.GREEN);
        sortButton.setMaximumSize(new Dimension(100, 20));
        numbers = generateRandomNumbers(count);
        sortButton.addActionListener(e -> {
            quickSort(numbers, 0, numbers.length - 1);
            if (isAscending) {
                isAscending = false;
            } else {
                reverseArray(numbers);
                isAscending = true;
            }
            refreshNumbersPanel();
        });

        JButton resetButton = new JButton("Reset");
        resetButton.setBackground(Color.GREEN);
        resetButton.setForeground(Color.WHITE);
        resetButton.setMaximumSize(new Dimension(100, 20));
        resetButton.addActionListener(e -> {
            numbersFrame.dispose();
            setVisible(true);
            numberInput.setText("");
        });

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add(sortButton);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(resetButton);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        wrapperPanel.add(controlPanel, BorderLayout.CENTER);

        numbersFrame.getContentPane().setLayout(new BorderLayout());
        numbersFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        numbersFrame.getContentPane().add(wrapperPanel, BorderLayout.EAST);

        refreshNumbersPanel();
        numbersFrame.setVisible(true);
        setVisible(false);
    }

    private void refreshNumbersPanel() {
        int ROWS_PER_COLUMN = 10;
        numbersPanel.removeAll();
        int columns = (int) Math.ceil((double) numbers.length / ROWS_PER_COLUMN);

        GroupLayout groupLayout = new GroupLayout(numbersPanel);
        numbersPanel.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        GroupLayout.SequentialGroup horizontalGroup = groupLayout.createSequentialGroup();
        GroupLayout.SequentialGroup verticalGroup = groupLayout.createSequentialGroup();

        JButton[][] buttons = new JButton[columns][ROWS_PER_COLUMN];
        for (int col = 0; col < columns; col++) {
            GroupLayout.ParallelGroup columnGroup = groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
            for (int row = 0; row < ROWS_PER_COLUMN; row++) {
                int index = row + col * ROWS_PER_COLUMN;
                if (index < numbers.length) {
                    JButton numberButton = new JButton(String.valueOf(numbers[index]));
                    numberButton.setBackground(Color.BLUE);
                    numberButton.setForeground(Color.WHITE);
                    numberButton.setMaximumSize(new Dimension(60, 25));
                    numberButton.addActionListener(e -> {
                        int selectedNumber = Integer.parseInt(numberButton.getText());
                        if (selectedNumber <= 30) {
                            numbers = generateRandomNumbers(count);
                            refreshNumbersPanel();
                        } else {
                            JOptionPane.showMessageDialog(null, "Please select a value smaller or equal to 30.");
                        }
                    });
                    buttons[col][row] = numberButton;
                    columnGroup.addComponent(numberButton);
                }
            }
            horizontalGroup.addGroup(columnGroup);
        }

        for (int row = 0; row < ROWS_PER_COLUMN; row++) {
            GroupLayout.ParallelGroup rowGroup = groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE);

            for (int col = 0; col < columns; col++) {
                if (buttons[col][row] != null) {
                    rowGroup.addComponent(buttons[col][row]);
                }
            }
            verticalGroup.addGroup(rowGroup);
        }

        groupLayout.setHorizontalGroup(horizontalGroup);
        groupLayout.setVerticalGroup(verticalGroup);

        numbersPanel.revalidate();
        numbersPanel.repaint();
    }

    private int[] generateRandomNumbers(int count) {
        int[] result = new int[count];
        Random rand = new Random();
        boolean isNumberLess30 = false;

        for (int i = 0; i < count; i++) {
            result[i] = rand.nextInt(1000) + 1;
            if (result[i] <= 30) {
                isNumberLess30 = true;
            }
        }

        if (!isNumberLess30) {
            result[rand.nextInt(count)] = rand.nextInt(30) + 1;
        }

        return result;
    }

    private void quickSort(int[] array, int first, int last) {
        if (first < last) {
            int supportingElement = partition(array, first, last);
            quickSort(array, first, supportingElement - 1);
            quickSort(array, supportingElement + 1, last);
        }
    }

    private int partition(int[] array, int first, int last) {
        int supportingElement = array[last];
        int i = (first - 1);
        for (int j = first; j < last; j++) {
            if (array[j] < supportingElement) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        int temp = array[i + 1];
        array[i + 1] = array[last];
        array[last] = temp;

        return i + 1;
    }

    private void reverseArray(int[] array) {
        int start = 0;
        int end = array.length - 1;
        while (start < end) {
            int temp = array[start];
            array[start] = array[end];
            array[end] = temp;
            start++;
            end--;
        }
    }

    public static void main(String[] args) {
        new SortingRandomNumbers().setVisible(true);
    }
}
