package com.fox.rgr_tf.controller;

import com.fox.rgr_tf.compiler.exceptions.Syntax;
import com.fox.rgr_tf.compiler.exceptions.SyntaxException;
import com.fox.rgr_tf.compiler.model.Lexeme;
import com.fox.rgr_tf.compiler.CodeParser;
import com.fox.rgr_tf.compiler.tree.MTree;
import com.fox.rgr_tf.compiler.tree.Tree;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class ViewController {
    // Таблица лексем
    @FXML
    public TextArea textAreaInput;
    @FXML
    public TableView<Lexeme> tableLexem;
    @FXML
    public TableColumn<Lexeme, Integer> columnId;
    @FXML
    public TableColumn<Lexeme, String> columnLexem;
    @FXML
    public TableColumn<Lexeme, String> columnType;

    // инициализируем форму данными
    private void createLexemTable() {
        // Добавление данных из модели
        ObservableList<Lexeme> Data = FXCollections.observableArrayList();
        for (Lexeme lexeme : CodeParser.getLexemeTable()) {
            Data.add(lexeme);
        }

        columnId.setCellValueFactory(new PropertyValueFactory<Lexeme, Integer>("id"));
        columnLexem.setCellValueFactory(new PropertyValueFactory<Lexeme, String>("name"));
        columnType.setCellValueFactory(new PropertyValueFactory<Lexeme, String>("type"));

        // заполняем таблицу данными
        tableLexem.setItems(Data);
    }


    // Таблица с HashMap
    private ObservableList<Lexeme> HashData = FXCollections.observableArrayList();
    @FXML
    public TableView<Lexeme> hashTable;
    @FXML
    public TableColumn<Lexeme, String> hashkey;
    @FXML
    public TableColumn<Lexeme, String> hashvalue;

    // инициализируем форму данными
    private void createHashTable() {
        // Добавление данных из модели
        ObservableList<Lexeme> Data = FXCollections.observableArrayList();
        CodeParser.getHash().forEach(
                (k, v) -> Data.add(new Lexeme(k, v)));

        hashkey.setCellValueFactory(new PropertyValueFactory<Lexeme, String>("name"));
        hashvalue.setCellValueFactory(new PropertyValueFactory<Lexeme, String>("type"));

        // заполняем таблицу данными
        hashTable.setItems(Data);
    }

    @FXML
    public TextArea generateTextField;

    private void generateCode() {
        //Tree tree = new Tree().getTreeForExp(CodeParser.getLexemeTable());
        //tree.setCmp(false);
        MTree tree = new MTree();
        tree.createTree(CodeParser.getLexemeTable(),tree.getRoot());
        generateTextField.setText(tree.toString());
    }


    public void buttonClick(MouseEvent mouseEvent) {
        if (!textAreaInput.getText().isBlank()) {
            String code = textAreaInput.getText();
            CodeParser.parseLexem(code);
            try {
                Syntax.syntaxAnalysis(CodeParser.getLexemeTable());
            } catch (SyntaxException e) {
                e.printStackTrace();
            }
            createLexemTable();
            createHashTable();
            generateCode();

        }
    }
}
