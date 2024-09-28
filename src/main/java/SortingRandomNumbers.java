import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Random;
import java.util.List;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;


public class SortingRandomNumbers extends JFrame {
    private static final int BORDER_NUMBER = 30;
    private static final int MAX_RANDOM_VALUE = 1000;
    private static final int MIN_RANDOM_VALUE = 1;
    private static final int ROWS_PER_COLUMN = 10;
    private static final Random RAND = new Random();
    private final JTextField numberInput;
    private int[] numbers;
    private int count;
    private boolean isAscending = true;
    private boolean isFirstClick = true;
    private JPanel numbersPanel;
    private JButton[][] buttons;

    public SortingRandomNumbers() {
        setTitle("Intro screen");
        setSize(300, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.add(Box.createVerticalStrut(100));

        JLabel inputLabel = new JLabel("How many numbers to display?");
        inputLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        numberInput = new JTextField();
        numberInput.setMaximumSize(new Dimension(90, 30));
        numberInput.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton enterButton = createButton("Enter", Color.white, Color.blue, new Dimension(90, 30));
        enterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        enterButton.addActionListener(e -> {
            String inputText = numberInput.getText();
            try {
                count = Integer.parseInt(inputText);

                if (count <= 0) {
                    showMessageDialog(null, "Please enter a positive number greater than 0");
                } else if (count > MAX_RANDOM_VALUE) {
                    showMessageDialog(null, "Please enter a number less than or equal to " + MAX_RANDOM_VALUE);
                } else {
                    showNumbersScreen(count);
                }

            } catch (NumberFormatException ex) {
                showMessageDialog(null, "Please enter a valid integer");
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
        numbersFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        numbersFrame.setLocationRelativeTo(null);

        numbersPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(numbersPanel);
        scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);

        numbers = generateRandomNumbers(count);
        JButton sortButton = createButton("Sort", Color.WHITE, Color.GREEN, new Dimension(100, 20));

        sortButton.addActionListener(e -> {
            if (isFirstClick) {
                highlightSorting(numbers, numbers.length - 1);
                isAscending = false;
                isFirstClick = false;
            } else {
                reverseArray(numbers);
                isAscending = !isAscending;
            }
            refreshNumbersPanel();
        });

        JButton resetButton = createButton("Reset", Color.WHITE, Color.GREEN, new Dimension(100, 20));
        resetButton.addActionListener(e -> {
            numbersFrame.dispose();
            setVisible(true);
            numberInput.setText("");
            isFirstClick = true;
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
        numbersPanel.removeAll();
        int columns = (int) Math.ceil((double) numbers.length / ROWS_PER_COLUMN);
        buttons = createButtonsNet(columns);

        GroupLayout groupLayout = new GroupLayout(numbersPanel);
        numbersPanel.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        GroupLayout.SequentialGroup horizontalGroup = groupLayout.createSequentialGroup();
        GroupLayout.SequentialGroup verticalGroup = groupLayout.createSequentialGroup();

        addColumns(horizontalGroup, buttons, columns, groupLayout);
        addRows(verticalGroup, buttons, groupLayout);

        groupLayout.setHorizontalGroup(horizontalGroup);
        groupLayout.setVerticalGroup(verticalGroup);

        numbersPanel.revalidate();
        numbersPanel.repaint();
    }

    private JButton[][] createButtonsNet(int columns) {
        JButton[][] jButtons = new JButton[columns][ROWS_PER_COLUMN];

        for (int col = 0; col < columns; col++) {
            for (int row = 0; row < ROWS_PER_COLUMN; row++) {
                int index = row + col * ROWS_PER_COLUMN;
                if (index < numbers.length) {
                    jButtons[col][row] = createNumberButton(numbers[index]);
                }
            }
        }
        return jButtons;
    }

    private JButton createNumberButton(int number) {
        JButton numberButton = createButton(String.valueOf(number),
                Color.WHITE, Color.BLUE, new Dimension(60, 25));
        numberButton.addActionListener(e -> {
            int selectedNumber = Integer.parseInt(numberButton.getText());
            if (selectedNumber <= BORDER_NUMBER) {
                numbers = generateRandomNumbers(selectedNumber);
                refreshNumbersPanel();
            } else {
                showMessageDialog(null, "Please select a value smaller or equal to " + BORDER_NUMBER);
            }
        });
        return numberButton;
    }

    private void addColumns(GroupLayout.SequentialGroup horizontalGroup, JButton[][] buttons,
                            int columns, GroupLayout groupLayout) {
        for (int col = 0; col < columns; col++) {
            GroupLayout.ParallelGroup columnGroup = groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
            for (int row = 0; row < buttons[col].length; row++) {
                if (buttons[col][row] != null) {
                    columnGroup.addComponent(buttons[col][row]);
                }
            }
            horizontalGroup.addGroup(columnGroup);
        }
    }

    private void addRows(GroupLayout.SequentialGroup verticalGroup, JButton[][] buttons,
                         GroupLayout groupLayout) {
        for (int row = 0; row < ROWS_PER_COLUMN; row++) {
            GroupLayout.ParallelGroup rowGroup = groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE);
            for (JButton[] buttonColumn : buttons) {
                if (buttonColumn[row] != null) {
                    rowGroup.addComponent(buttonColumn[row]);
                }
            }
            verticalGroup.addGroup(rowGroup);
        }
    }

    private int[] generateRandomNumbers(int count) {
        int[] result = new int[count];
        boolean isNumberLess30 = false;

        for (int i = 0; i < count; i++) {
            result[i] = RAND.nextInt(MAX_RANDOM_VALUE) + MIN_RANDOM_VALUE;
            if (result[i] <= BORDER_NUMBER) {
                isNumberLess30 = true;
            }
        }

        if (!isNumberLess30) {
            result[RAND.nextInt(count)] = RAND.nextInt(BORDER_NUMBER) + MIN_RANDOM_VALUE;
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
        highlightOneElement(last);
        int i = first;

        for (int j = first; j < last; j++) {
            highlightElements(i, j, Color.YELLOW);
            if (array[j] < supportingElement) {
                swap(array, i, j);
                updateButtons(array.clone());
                highlightElements(i, j, Color.RED);
                i++;
            }
        }

        swap(array, i, last);
        updateButtons(array.clone());
        highlightElements(i, last, Color.GREEN);

        return i;
    }

    private void swap(int[] array, int index1, int index2) {
        int temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    private void highlightElements(int index1, int index2, Color color) {
        highlight(index1, color);
        highlight(index2, color);
    }

    private void highlightOneElement(int index) {
        highlight(index, Color.BLACK);
    }

    private void highlight(int index, Color color) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        buttons[index / ROWS_PER_COLUMN][index % ROWS_PER_COLUMN].setBackground(color);
        numbersPanel.revalidate();
        numbersPanel.repaint();
    }

    private void updateButtons(int[] latestArray) {
        for (int col = 0; col < buttons.length; col++) {
            for (int row = 0; row < buttons[col].length; row++) {
                int index = row + col * ROWS_PER_COLUMN;
                if (index < latestArray.length) {
                    buttons[col][row].setText(String.valueOf(latestArray[index]));
                }
            }
        }
        numbersPanel.revalidate();
        numbersPanel.repaint();
    }

    private void highlightSorting(int[] array, int last) {
        SwingWorker<Void, int[]> sorter = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                quickSort(array, 0, last);
                return null;
            }

            @Override
            protected void process(List<int[]> pieces) {
                int[] latestArray = pieces.get(pieces.size() - 1);
                updateButtons(latestArray);
            }

            @Override
            protected void done() {
                resetButtonColors();
            }

        };
        sorter.execute();
    }

    private void resetButtonColors() {
        for (JButton[] buttonColumn : buttons) {
            for (JButton button : buttonColumn) {
                if (button != null) {
                    button.setBackground(Color.BLUE);
                }
            }
        }
        numbersPanel.revalidate();
        numbersPanel.repaint();
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

    private JButton createButton(String name, Color foreground, Color background, Dimension maxSize) {
        JButton button = new JButton(name);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setMaximumSize(maxSize);

        return button;
    }

    public static void main(String[] args) {
        new SortingRandomNumbers().setVisible(true);
    }
}
