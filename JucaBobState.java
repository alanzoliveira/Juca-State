import java.util.Scanner;

interface JucaState {
    void enter(Juca juca);
    void execute(Juca juca);
    void leave(Juca juca);
}

class Juca {

    JucaState state;
    int hunger = 0;
    int fatigue = 0;

    Juca() {
        changeState(new WorkingState());
    }

    void changeState(JucaState newState) {
        if (state != null) {
            state.leave(this);
        }
        state = newState;
        state.enter(this);
    }

    void tick() {
        state.execute(this);
    }

    void clampValues() {
        hunger = Math.max(hunger, 0);
        fatigue = Math.max(fatigue, 0);
    }

    void printStatus(String label) {
        System.out.println("[Juca] " + label);
        System.out.println("[Juca] Fome: " + hunger);
        System.out.println("[Juca] Cansaço: " + fatigue);
    }
}

class WorkingState implements JucaState {

    public void enter(Juca juca) {
        System.out.println("[Juca] Hora de ir para o trabalho!");
    }

    public void execute(Juca juca) {
        juca.hunger += 2;
        juca.fatigue += 5;
        juca.clampValues();
        juca.printStatus("Trabalhando...");

        if (juca.fatigue > 50) {
            juca.changeState(new SleepingState());
        } else if (juca.hunger > 10) {
            juca.changeState(new EatingState());
        }
    }

    public void leave(Juca juca) {
    }
}

class EatingState implements JucaState {

    public void enter(Juca juca) {
        System.out.println("[Juca] Bateu uma fome...");
    }

    public void execute(Juca juca) {
        juca.hunger -= 5;
        juca.clampValues();
        juca.printStatus("Comendo...");

        if (juca.hunger <= 0) {
            juca.changeState(new WorkingState());
        }
    }

    public void leave(Juca juca) {
        System.out.println("[Juca] Ufa! Já estou cheio...");
    }
}

class SleepingState implements JucaState {

    public void enter(Juca juca) {
        System.out.println("[Juca] Bateu um sono...");
    }

    public void execute(Juca juca) {
        juca.hunger += 1;
        juca.fatigue -= 10;
        juca.clampValues();
        juca.printStatus("Dormindo...");

        if (juca.fatigue <= 0) {
            if (juca.hunger <= 10) {
                juca.changeState(new WorkingState());
            } else {
                juca.changeState(new EatingState());
            }
        }
    }

    public void leave(Juca juca) {
    }
}

interface BobState {
    void enter(Bob bob);
    void execute(Bob bob);
    void leave(Bob bob);
}

class Bob {

    BobState state;
    int cookingProgress = 0;
    int mess = 0;

    Bob() {
        changeState(new CookingState());
    }

    void changeState(BobState newState) {
        if (state != null) {
            state.leave(this);
        }
        state = newState;
        state.enter(this);
    }

    void tick() {
        state.execute(this);
    }

    void clampValues() {
        cookingProgress = Math.max(cookingProgress, 0);
        mess = Math.max(mess, 0);
    }

    void printStatus(String label) {
        System.out.println("[Bob] " + label);
        System.out.println("[Bob] Progresso da comida: " + cookingProgress);
        System.out.println("[Bob] Bagunça: " + mess);
    }
}

class CookingState implements BobState {

    public void enter(Bob bob) {
        System.out.println("[Bob] Hora de preparar uma refeição!");
    }

    public void execute(Bob bob) {
        bob.cookingProgress += 3;
        bob.mess += 2;
        bob.clampValues();
        bob.printStatus("Cozinhando...");

        if (bob.cookingProgress >= 12) {
            bob.cookingProgress = 12;
            bob.changeState(new CleaningState());
        }
    }

    public void leave(Bob bob) {
        System.out.println("[Bob] A comida está pronta!");
    }
}

class CleaningState implements BobState {

    public void enter(Bob bob) {
        System.out.println("[Bob] Agora preciso limpar esta bagunça...");
    }

    public void execute(Bob bob) {
        bob.mess -= 4;
        bob.clampValues();
        bob.printStatus("Limpando...");

        if (bob.mess <= 0) {
            bob.changeState(new CookingState());
        }
    }

    public void leave(Bob bob) {
        System.out.println("[Bob] Tudo limpo!");
        bob.cookingProgress = 0;
    }
}

public class SimulacaoJucaEBobState {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Quantos ticks a simulação deve executar? ");
        int totalTicks = scanner.nextInt();
        scanner.close();

        Juca juca = new Juca();
        Bob bob = new Bob();

        for (int i = 1; i <= totalTicks; i++) {
            System.out.println("=== Tick " + i + " ===");
            juca.tick();
            bob.tick();
            System.out.println();
        }
    }
}
