package actors;

import akka.actor.typed.*;
import akka.actor.typed.javadsl.*;

public class SupervisorActor extends AbstractBehavior<SupervisorActor.Command> {

    public interface Command {}

    public static class AssignTask implements Command {
        public final int a;
        public final int b;
        public final ActorRef<WorkerActor.Result> replyTo;
        public AssignTask(int a, int b, ActorRef<WorkerActor.Result> replyTo) {
            this.a = a;
            this.b = b;
            this.replyTo = replyTo;
        }
    }

    public static Behavior<Command> create() {
        return Behaviors.supervise(
                Behaviors.setup(SupervisorActor::new)
        ).onFailure(RuntimeException.class, SupervisorStrategy.restart());
    }

    private final ActorRef<WorkerActor.Command> worker;

    private SupervisorActor(ActorContext<Command> context) {
        super(context);
        worker = context.spawn(WorkerActor.create(), "workerActor");
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(AssignTask.class, this::onAssignTask)
                .build();
    }

    private Behavior<Command> onAssignTask(AssignTask command) {
        worker.tell(new WorkerActor.ProcessTask(command.a, command.b, command.replyTo));
        return this;
    }
}
