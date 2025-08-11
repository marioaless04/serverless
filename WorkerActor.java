package actors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class WorkerActor extends AbstractBehavior<WorkerActor.Command> {

    public interface Command {}

    public static class ProcessTask implements Command {
        public final int number1;
        public final int number2;
        public final ActorRef<Result> replyTo;

        public ProcessTask(int number1, int number2, ActorRef<Result> replyTo) {
            this.number1 = number1;
            this.number2 = number2;
            this.replyTo = replyTo;
        }
    }

    public static class Result {
        public final int value;
        public Result(int value) {
            this.value = value;
        }
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(WorkerActor::new);
    }

    private WorkerActor(ActorContext<Command> context) {
        super(context);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(ProcessTask.class, this::onProcessTask)
                .build();
    }

    private Behavior<Command> onProcessTask(ProcessTask command) {
        if (command.number1 == 13) {
            throw new RuntimeException("Error intencional para prueba");
        }
        int sum = command.number1 + command.number2;
        command.replyTo.tell(new Result(sum));
        return this;
    }
}
