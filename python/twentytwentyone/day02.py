from utils import read_file_to_tuple
from functools import reduce


def run():
    commands = read_file_to_tuple('inputs/day02.txt')
    parsed_commands = [(cmd.split()[0], int(cmd.split()[1])) for cmd in commands]
    pos, depth, _ = handle_all_tuples2(parsed_commands)
    print(pos * depth)


def handle_tuple(command_tuple, coordinates):
    pos, depth = coordinates
    match command_tuple[0]:
        case 'forward':
            return pos + command_tuple[1], depth
        case 'up':
            return pos, depth - command_tuple[1]
        case 'down':
            return pos, depth + command_tuple[1]


def handle_tuple_part2(command_tuple, coordinates):
    pos, depth, aim = coordinates
    delta = command_tuple[1]
    match command_tuple[0]:
        case 'forward':
            return pos + delta, depth + aim * delta, aim
        case 'up':
            return pos, depth, aim - delta
        case 'down':
            return pos, depth, aim + delta


def handle_all_tuples(command_tuples):
    return reduce(lambda coords, command: handle_tuple(command, coords), command_tuples, (0, 0))


def handle_all_tuples2(command_tuples):
    return reduce(lambda coords, command: handle_tuple_part2(command, coords), command_tuples, (0, 0, 0))
