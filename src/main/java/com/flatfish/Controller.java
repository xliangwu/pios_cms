package com.flatfish;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Controller {

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Label statusLabel;

    @FXML
    public void RecordExport(ActionEvent event) {
        System.out.println("Click :" + event.getSource());

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        System.out.println("Start date :" + startDate);
        System.out.println("End date :" + endDate);

        LocalDateTime now = LocalDateTime.now();
        String timeLabel = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss "));
        if (null == startDate || null == endDate) {
            statusLabel.setTextFill(Color.web("#E33D51"));
            statusLabel.setText(timeLabel + "请输入开始时间和结束时间");
        } else {
            PassExportTask task = new PassExportTask();
            task.exportToFile(startDate, endDate);

            statusLabel.setTextFill(Color.web("#000000"));
            statusLabel.setText(timeLabel + "数据导出成功");
        }
    }
}
