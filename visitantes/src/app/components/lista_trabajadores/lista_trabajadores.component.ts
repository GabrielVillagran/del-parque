import {Component, OnInit} from '@angular/core';
import {TrabajadoresService} from "../../services/trabajadores.service";
import {Trabajador} from "../../models/trabajador";
import Swal from "sweetalert2";

@Component({
  selector: 'app-lista_trabajadores',
  templateUrl: './lista_trabajadores.component.html',
  styleUrls: ['./lista_trabajadores.component.css']
})
export class ListaTrabajadoresComponent implements OnInit {
  // datos usados para realizar la busqueda
  name: string | undefined;
  trabajador: Trabajador = {} as Trabajador;
  trabajadores: Trabajador[] = [];
  page: number = 0;
  isLoading: boolean = true;
  listOfPages: number[] = [];

  constructor(private trabajadoresService: TrabajadoresService,) {
  }

  ngOnInit(): void {

    this.trabajadoresService.getTrabajadores(this.page).subscribe(trabajadores => {
      this.trabajadores = trabajadores.content;
      const totalOfPages = Math.trunc(trabajadores.totalElements / trabajadores.size);
      for (let i = 0; i <= totalOfPages; i++) {
        this.listOfPages.push(i + 1);
      }
      this.isLoading = false;
    })

  }

  updatePage(page: number) {
    this.isLoading = true;
    this.trabajadoresService.getTrabajadores(page).subscribe(trabajadores => {
      this.trabajadores = trabajadores.content;
      this.isLoading = false;
    })
  }

  delete(id: string) {
    Swal.fire({
      title: `Estas seguro que deseas borrar este trabajador?`,
      icon: 'warning',
      showDenyButton: true,
      confirmButtonText: `Si`,
      cancelButtonText: `No`
    }).then((value) => {
      if (value.isConfirmed) {
        this.trabajadoresService.delete(id).subscribe(() => {
          Swal.fire({
            title: `El trabajador fue borrado correctamente`,
            icon: 'success',
            showDenyButton: false,
            showCancelButton: false,
            confirmButtonText: `Cerrar`
          }).then(() => {
            this.updatePage(this.page);
          })
        })
      }
    });
  }
}
