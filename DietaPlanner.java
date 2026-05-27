import java.util.Scanner;
import java.text.DecimalFormat;

public class DietaPlanner {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DecimalFormat df = new DecimalFormat("0.00");

        System.out.println("=== Planejador de Dieta ===");
        System.out.print("Peso (kg): ");
        double peso = sc.nextDouble();
        System.out.print("Altura (cm): ");
        double alturaCm = sc.nextDouble();
        System.out.print("Idade: ");
        int idade = sc.nextInt();
        sc.nextLine();
        System.out.print("Sexo (M/F): ");
        String sexo = sc.nextLine().trim();
        System.out.print("Objetivo (1- Perder peso, 2- Manter, 3- Ganhar massa): ");
        int objetivo = sc.nextInt();
        System.out.print("Quantidade de massa muscular (kg): ");
        double massaMuscular = sc.nextDouble();
        System.out.print("Quantidade de massa gorda (kg): ");
        double massaGorda = sc.nextDouble();

        System.out.println("\nNível de atividade física:");
        System.out.println("1 - Sedentário (pouco ou nenhum exercício)");
        System.out.println("2 - Leve (exercício leve 1-3 dias/sem)");
        System.out.println("3 - Moderado (3-5 dias/sem)");
        System.out.println("4 - Ativo (6-7 dias/sem)");
        System.out.println("5 - Muito ativo (trabalho físico intenso ou treino duas vezes/dia)");
        System.out.print("Escolha (1-5): ");
        int nivel = sc.nextInt();

        double altura = alturaCm; // já em cm para fórmulas

        double bmr;
        if (sexo.equalsIgnoreCase("M") || sexo.equalsIgnoreCase("Masculino")) {
            bmr = 10 * peso + 6.25 * altura - 5 * idade + 5; // Mifflin-St Jeor (homens)
        } else {
            bmr = 10 * peso + 6.25 * altura - 5 * idade - 161; // mulheres
        }

        double fatorAtividade;
        switch (nivel) {
            case 1: fatorAtividade = 1.2; break;
            case 2: fatorAtividade = 1.375; break;
            case 3: fatorAtividade = 1.55; break;
            case 4: fatorAtividade = 1.725; break;
            case 5: fatorAtividade = 1.9; break;
            default: fatorAtividade = 1.2; break;
        }

        double tdee = bmr * fatorAtividade; // gasto energético diário estimado

        double aguaLitros = peso * 0.035; // 35 ml por kg

        double targetCalories;
        switch (objetivo) {
            case 1: // perder peso
                targetCalories = tdee - 500; // déficit moderado
                break;
            case 3: // ganhar massa
                targetCalories = tdee + 350; // leve superávit
                break;
            default: // manter
                targetCalories = tdee;
                break;
        }

        // Proteína: ajusta conforme objetivo (g por kg de peso corporal)
        double proteinaPorKg;
        if (objetivo == 3) // ganhar massa
            proteinaPorKg = 2.2;
        else if (objetivo == 1) // perder peso
            proteinaPorKg = 2.4;
        else
            proteinaPorKg = 1.8; // manter

        double proteinaGramas = proteinaPorKg * peso;
        double proteinaKcal = proteinaGramas * 4;

        // Gordura: usar ~25% das calorias
        double gorduraKcal = targetCalories * 0.25;
        double gorduraGramas = gorduraKcal / 9.0;

        // Carboidratos: resto das calorias
        double carbKcal = targetCalories - proteinaKcal - gorduraKcal;
        if (carbKcal < 0) carbKcal = 0; // segurança
        double carbGramas = carbKcal / 4.0;

        double percentualGordura = (massaGorda / peso) * 100.0;

        double proteinaPercent = 0.0, gorduraPercent = 0.0, carbPercent = 0.0;
        if (targetCalories > 0) {
            proteinaPercent = (proteinaKcal / targetCalories) * 100.0;
            gorduraPercent = (gorduraKcal / targetCalories) * 100.0;
            carbPercent = (carbKcal / targetCalories) * 100.0;
        }

        System.out.println("\n--- Resultado ---");
        System.out.println("Peso: " + df.format(peso) + " kg");
        System.out.println("Altura: " + df.format(alturaCm) + " cm");
        System.out.println("Idade: " + idade + " anos");
        System.out.println("Massa muscular: " + df.format(massaMuscular) + " kg");
        System.out.println("Massa gorda: " + df.format(massaGorda) + " kg (" + df.format(percentualGordura) + "%)");
        System.out.println("BMR (Taxa metabólica basal): " + df.format(bmr) + " kcal/dia");
        System.out.println("TDEE (Gasto diário estimado): " + df.format(tdee) + " kcal/dia");
        System.out.println("Ingestão água indicada: " + df.format(aguaLitros) + " L/dia (≈ " + df.format(aguaLitros*1000) + " ml)");
        System.out.println("Calorias alvo (segundo objetivo): " + df.format(targetCalories) + " kcal/dia");

        System.out.println("\nMacros (kcal / % do total):");
        System.out.println("Proteína: " + df.format(proteinaKcal) + " kcal (" + df.format(proteinaPercent) + "%), " + df.format(proteinaGramas) + " g");
        System.out.println("Gordura: " + df.format(gorduraKcal) + " kcal (" + df.format(gorduraPercent) + "%), " + df.format(gorduraGramas) + " g");
        System.out.println("Carboidratos: " + df.format(carbKcal) + " kcal (" + df.format(carbPercent) + "%), " + df.format(carbGramas) + " g");

        // Distribuição por refeição (exemplo)
        System.out.println("\nExemplo de distribuição por refeição:");
        double[] perc = {0.25, 0.35, 0.30, 0.10}; // café, almoço, jantar, lanches
        String[] nomes = {"Café da manhã", "Almoço", "Jantar", "Lanches"};
        for (int i = 0; i < perc.length; i++) {
            double cal = targetCalories * perc[i];
            double pKcal = proteinaKcal * perc[i];
            double gKcal = gorduraKcal * perc[i];
            double cKcal = cal - pKcal - gKcal;
            if (cKcal < 0) cKcal = 0;
            double pG = (pKcal / 4.0);
            double gG = (gKcal / 9.0);
            double cG = (cKcal / 4.0);
            System.out.println(nomes[i] + ": " + df.format(cal) + " kcal — Proteína: " + df.format(pG) + " g, Gordura: " + df.format(gG) + " g, Carb: " + df.format(cG) + " g");
        }

        System.out.println("\nObservações:");
        System.out.println("- Ajuste os valores com acompanhamento profissional.");
        System.out.println("- Para perda de peso, preserve a proteína e ajuste treinos.");
        System.out.println("- Para ganho de massa, combine superávit calórico com treino de força.");

        sc.close();
    }

}
