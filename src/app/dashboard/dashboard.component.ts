import { Component, OnInit } from '@angular/core';
import { DocumentService } from 'app/services/document.service';
import * as Chartist from 'chartist';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  documentsEntrants: any[] = [];
  documentsSortants: any[] = [];

  constructor(private documentService: DocumentService) { }

  ngOnInit() {
    // Charger les documents
    this.loadDocuments();

    // Initialiser les graphiques
    this.initCharts();
  }

  loadDocuments() {
  this.documentService.getMesCourriersEntrants().subscribe({
    next: data => this.documentsEntrants = data,
    error: err => console.error(err)
  });

  this.documentService.getMesCourriersSortants().subscribe({
    next: data => this.documentsSortants = data,
    error: err => console.error(err)
  });
}

  startAnimationForLineChart(chart: any) {
    let seq: any = 0, delays: any = 80, durations: any = 500;

    chart.on('draw', function(data: any) {
      if (data.type === 'line' || data.type === 'area') {
        data.element.animate({
          d: {
            begin: 600,
            dur: 700,
            from: data.path.clone().scale(1, 0).translate(0, data.chartRect.height()).stringify(),
            to: data.path.clone().stringify(),
            easing: Chartist.Svg.Easing.easeOutQuint
          }
        });
      } else if (data.type === 'point') {
        seq++;
        data.element.animate({
          opacity: {
            begin: seq * delays,
            dur: durations,
            from: 0,
            to: 1,
            easing: 'ease'
          }
        });
      }
    });

    seq = 0;
  }

  startAnimationForBarChart(chart: any) {
    let seq2: any = 0, delays2: any = 80, durations2: any = 500;

    chart.on('draw', function(data: any) {
      if (data.type === 'bar') {
        seq2++;
        data.element.animate({
          opacity: {
            begin: seq2 * delays2,
            dur: durations2,
            from: 0,
            to: 1,
            easing: 'ease'
          }
        });
      }
    });

    seq2 = 0;
  }

  initCharts() {
    /* ----------========== Daily Sales Chart ==========---------- */
    const dataDailySalesChart: any = {
      labels: ['M', 'T', 'W', 'T', 'F', 'S', 'S'],
      series: [[12, 17, 7, 17, 23, 18, 38]]
    };
    const optionsDailySalesChart: any = {
      lineSmooth: Chartist.Interpolation.cardinal({ tension: 0 }),
      low: 0,
      high: 50,
      chartPadding: { top: 0, right: 0, bottom: 0, left: 0 }
    };
    var dailySalesChart = new Chartist.Line('#dailySalesChart', dataDailySalesChart, optionsDailySalesChart);
    this.startAnimationForLineChart(dailySalesChart);

    /* ----------========== Completed Tasks Chart ==========---------- */
    const dataCompletedTasksChart: any = {
      labels: ['12p', '3p', '6p', '9p', '12p', '3a', '6a', '9a'],
      series: [[230, 750, 450, 300, 280, 240, 200, 190]]
    };
    const optionsCompletedTasksChart: any = {
      lineSmooth: Chartist.Interpolation.cardinal({ tension: 0 }),
      low: 0,
      high: 1000,
      chartPadding: { top: 0, right: 0, bottom: 0, left: 0 }
    };
    var completedTasksChart = new Chartist.Line('#completedTasksChart', dataCompletedTasksChart, optionsCompletedTasksChart);
    this.startAnimationForLineChart(completedTasksChart);

    /* ----------========== Emails Subscription Chart ==========---------- */
    var datawebsiteViewsChart = {
      labels: ['J', 'F', 'M', 'A', 'M', 'J', 'J', 'A', 'S', 'O', 'N', 'D'],
      series: [[542, 443, 320, 780, 553, 453, 326, 434, 568, 610, 756, 895]]
    };
    var optionswebsiteViewsChart = {
      axisX: { showGrid: false },
      low: 0,
      high: 1000,
      chartPadding: { top: 0, right: 5, bottom: 0, left: 0 }
    };
    var responsiveOptions: any[] = [
      ['screen and (max-width: 640px)', {
        seriesBarDistance: 5,
        axisX: {
          labelInterpolationFnc: function(value: any) { return value[0]; }
        }
      }]
    ];
    var websiteViewsChart = new Chartist.Bar('#websiteViewsChart', datawebsiteViewsChart, optionswebsiteViewsChart, responsiveOptions);
    this.startAnimationForBarChart(websiteViewsChart);
  }
}
