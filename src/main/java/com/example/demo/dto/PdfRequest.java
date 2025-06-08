

package com.example.demo.dto; 

import java.util.List;

public class PdfRequest {
    private String message;
    private List<ChartData> charts;

    
    public PdfRequest() {
    }

    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ChartData> getCharts() {
        return charts;
    }

    public void setCharts(List<ChartData> charts) {
        this.charts = charts;
    }

    
    public static class ChartData {
        private String title;
        private String dataUrl; 

        
        public ChartData() {
        }

        
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDataUrl() {
            return dataUrl;
        }

        public void setDataUrl(String dataUrl) {
            this.dataUrl = dataUrl;
        }
    }
}