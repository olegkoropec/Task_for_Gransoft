import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class SortingRandomNumbers extends JFrame {
    private final JTextField numberInput;
    private JPanel numbersPanel;
    private int[] numbers;
    private boolean isAscending = true;

    public SortingRandomNumbers() {
//        setTitle("Intro screen");
        setSize(300, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.add(Box.createVerticalStrut(150));

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
                int count = Integer.parseInt(inputText);
                if (count > 0 && count <= 50) {
                    showNumbersScreen(count);
                } else {
                    JOptionPane.showMessageDialog(null, "Enter a valid number between 1 and 50.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.");
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
        numbers = generateRandomNumbers(count);

        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());
        int columns = (int) Math.ceil(count / 10.0);

//        numbersPanel = new JPanel(new GridLayout(10, columns, 1, 1));

        numbersPanel = new JPanel();
        numbersPanel.setLayout(new BoxLayout(numbersPanel, BoxLayout.Y_AXIS));

        for (int num : numbers) {
            JButton numberButton = new JButton(String.valueOf(num));
            numberButton.setBackground(Color.BLUE);
            numberButton.setForeground(Color.WHITE);
            numberButton.setMaximumSize(new Dimension(60, 25));
            numberButton.addActionListener(e -> {
                int selectedNumber = Integer.parseInt(numberButton.getText());
                if (selectedNumber <= 30) {
                    numbers = generateRandomNumbers(count); // Нові числа
                    refreshNumbersPanel();
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a value smaller or equal to 30.");
                }
            });
            numbersPanel.add(numberButton);
            numbersPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JButton sortButton = new JButton("Sort");
        sortButton.setForeground(Color.WHITE);
        sortButton.setBackground(Color.GREEN);
        sortButton.setMaximumSize(new Dimension(100, 20));
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
            getContentPane().removeAll();
            add(new SortingRandomNumbers().getContentPane());
            revalidate();
            repaint();
        });

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add(sortButton);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(resetButton);

        add(numbersPanel);
        add(controlPanel, BorderLayout.EAST);

        revalidate();
        repaint();
    }

    private void refreshNumbersPanel() {
        numbersPanel.removeAll();
        for (int num : numbers) {
            JButton numberButton = new JButton(String.valueOf(num));
            numberButton.setBackground(Color.BLUE);
            numberButton.setForeground(Color.WHITE);
            numberButton.setMaximumSize(new Dimension(60, 25));
            numberButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedNumber = Integer.parseInt(numberButton.getText());
                    if (selectedNumber <= 30) {
                        numbers = generateRandomNumbers(numbers.length);
                        refreshNumbersPanel();
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select a value smaller or equal to 30.");
                    }
                }
            });
            numbersPanel.add(numberButton);
            numbersPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        revalidate();
        repaint();
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

    private void quickSort(int[] array, int left, int right) {
        if (left < right) {
            int supportingElement = partition(array, left, right);
            quickSort(array, left, supportingElement - 1);
            quickSort(array, supportingElement + 1, right);
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


