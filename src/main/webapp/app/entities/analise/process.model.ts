/**
 * Created by Wizi on 21.05.2017.
 */

import { FatorAjuste, TipoFatorAjuste} from '../fator-ajuste/fator-ajuste.model';
import { Funcionalidade} from '../funcionalidade';
import { Modulo } from '../../entities/modulo';
import {Complexity, LogicalFile, OutputTypes} from "./enums";
import {FuncaoTransacao} from "../funcao-transacao/funcao-transacao.model";
import {MetodoContagem} from "./analise.model";



export class Process{
    public id:number;
    public factor?:FatorAjuste;
    public module:Modulo;
    public func:Funcionalidade;
    public name:String;
    public classification:number;
    public ret:number;
    public det:number;
    public complexity:Complexity;
    public pf:number;
    public grossPF:number;
    public retStr:String;
    public detStr:String;
    public sustantation:String;

    public files:UploadedFile[]=[];


    private INDICATIVE_ILF_FP:number=35;
    private INDICATIVE_EIF_FP:number=15;


    private applyFactor(){
        this.grossPF = this.pf;
        if (this.factor==null) {
            return;
        }

        let factorValue:number = 0;
        if (this.factor.tipoAjuste.toString()=='PERCENTUAL'){
            factorValue = this.pf*this.factor.fator;
        } else {
            this.complexity = Complexity.NONE;
            factorValue = this.factor.fator;
        }
        this.pf=factorValue;
    }


    private checkIsNumber(val:String){
        return !isNaN(Number(val));
    }

    public calculateRetDet(){
        if (this.checkIsNumber(this.detStr)) {
            this.det = Number(this.detStr);
        } else {
            this.det=this.detStr.split("\n", 1000).filter(e=>e.split(' ').join('').length!=0).length;
        }

        if (this.checkIsNumber(this.retStr)) {
            this.ret = Number(this.retStr);
        } else {
            this.ret=this.retStr.split("\n", 1000).filter(e=>e.split(' ').join('').length!=0).length;
        }

    }

    public calculate(countingType:MetodoContagem){


        if (countingType.toString()=="INDICATIVA"){
            this.retStr="";
            this.detStr="";
            this.ret=0;
            this.det=0;
            if (this.classification == LogicalFile.ILF) {
                this.pf = this.INDICATIVE_ILF_FP;
            } else {
                this.pf = this.INDICATIVE_EIF_FP;
            }
            this.complexity = Complexity.NONE;
            this.applyFactor();
            return;
        }

        this.calculateRetDet();
        this.complexity = Complexity.LOW;
        if (countingType.toString()=="ESTIMADA"){ // If couting type is ESTMADA - Complexity is Medium
            this.complexity = Complexity.LOW;
        } else { // Calculate Complexity by REt AND DET values

            if (this.ret == 1) {
                if (this.det <= 50) {
                    this.complexity = Complexity.LOW;
                } else {
                    this.complexity = Complexity.MEDIUM;
                }
            }

            if (this.ret >= 2 && this.ret <= 5) {
                if (this.det <= 19) {
                    this.complexity = Complexity.LOW;
                }

                if (this.det >= 20 && this.det <= 50) {
                    this.complexity = Complexity.MEDIUM;
                }
                if (this.det >= 51) {
                    this.complexity = Complexity.HIGH;
                }
            }

            if (this.ret >= 6) {
                if (this.det <= 19) {
                    this.complexity = Complexity.MEDIUM;
                }

                if (this.det >= 20) {
                    this.complexity = Complexity.HIGH;
                }
            }
        }
        if (this.classification == LogicalFile.ILF) {

            switch (this.complexity) {
                case Complexity.LOW:{
                    this.pf=7;
                    break;
                }
                case Complexity.MEDIUM:{
                    this.pf=10;
                    break;
                }
                case Complexity.HIGH:{
                    this.pf=15;
                    break;
                }
                default: this.pf=7;
            }
        } else {
            switch (this.complexity) {
                case Complexity.LOW:{
                    this.pf=5;
                    break;
                }
                case Complexity.MEDIUM:{
                    this.pf=7;
                    break;
                }
                case Complexity.HIGH:{
                    this.pf=10;
                    break;
                }
                default: this.pf=5;
            }
        }

        this.applyFactor();
    }


    public calculateTran(countingType:MetodoContagem){
        this.complexity = Complexity.LOW;
        this.calculateRetDet();
        if (countingType.toString()=="ESTIMADA"){ // If couting type is ESTMADA - Complexity is Medium
            this.complexity = Complexity.MEDIUM;
        } else {
            if (this.classification == OutputTypes.EO || this.classification == OutputTypes.EI) {

                if (this.ret == 0 || this.ret == 1) {
                    if (this.det <= 15) {
                        this.complexity = Complexity.LOW;
                    } else {
                        this.complexity = Complexity.MEDIUM;
                    }
                }

                if (this.ret == 2) {
                    if (this.det <= 4) {
                        this.complexity = Complexity.LOW;
                    }

                    if (this.det >= 5 && this.det <= 15) {
                        this.complexity = Complexity.MEDIUM;
                    }
                    if (this.det >= 16) {
                        this.complexity = Complexity.HIGH;
                    }
                }


                if (this.ret >= 3) {
                    if (this.det <= 4) {
                        this.complexity = Complexity.MEDIUM;
                    }

                    if (this.det >= 5) {
                        this.complexity = Complexity.HIGH;
                    }
                }

            } else {

                if (this.ret == 0 || this.ret == 1) {
                    if (this.det <= 19) {
                        this.complexity = Complexity.LOW;
                    } else {
                        this.complexity = Complexity.MEDIUM;
                    }
                }

                if (this.ret == 2 || this.ret == 3) {
                    if (this.det <= 5) {
                        this.complexity = Complexity.LOW;
                    }

                    if (this.det >= 6 && this.det <= 19) {
                        this.complexity = Complexity.MEDIUM;
                    }
                    if (this.det >= 20) {
                        this.complexity = Complexity.HIGH;
                    }
                }


                if (this.ret >= 4) {
                    if (this.det <= 5) {
                        this.complexity = Complexity.MEDIUM;
                    }

                    if (this.det >= 6) {
                        this.complexity = Complexity.HIGH;
                    }
                }


            }
        }
        if (this.classification == OutputTypes.EI || this.classification == OutputTypes.EQ) {

            switch (this.complexity) {
                case Complexity.LOW:{
                    this.pf=3;
                    break;
                }
                case Complexity.MEDIUM:{
                    this.pf=4;
                    break;
                }
                case Complexity.HIGH:{
                    this.pf=5;
                    break;
                }
                default: this.pf=3;
            }
        } else {
            switch (this.complexity) {
                case Complexity.LOW:{
                    this.pf=4;
                    break;
                }
                case Complexity.MEDIUM:{
                    this.pf=5;
                    break;
                }
                case Complexity.HIGH:{
                    this.pf=7;
                    break;
                }
                default: this.pf=4;
            }
        }

        this.applyFactor();
    }


    public convertFromTransacao(funcaoTransacao:FuncaoTransacao,countingType:MetodoContagem){
        this.id = funcaoTransacao.id;
        this.pf = funcaoTransacao.pf;
        this.grossPF = funcaoTransacao.grossPF;
        this.func = funcaoTransacao.funcionalidade;
        this.factor = funcaoTransacao.fatorAjuste;
        this.detStr = funcaoTransacao.detStr;
        this.retStr = funcaoTransacao.ftrStr;
        this.module = funcaoTransacao.funcionalidade.modulo;
        this.name = funcaoTransacao.name;
        this.sustantation = funcaoTransacao.sustantation;
        this.files = [].concat(funcaoTransacao.files);

        if (funcaoTransacao.tipo.toString()=="EE") {
            this.classification = OutputTypes.EI;
        } else if (funcaoTransacao.tipo.toString()=="SE"){
            this.classification = OutputTypes.EO;
        } else {
            this.classification = OutputTypes.EQ;
        }


        this.calculateTran(countingType);
    }

}

//Class that described uploaded file
export class UploadedFile{
    public id:number;
    public originalName:String;
    public filename:String;
    public dateOf:Date;
    public sizeOf:number;
    public processId:number;
    public processType:number;



}
